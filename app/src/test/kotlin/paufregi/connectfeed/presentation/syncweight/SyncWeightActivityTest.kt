package paufregi.connectfeed.presentation.syncweight

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runEmptyComposeUiTest
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.runBlocking
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import paufregi.connectfeed.FakeAndroidKeyStore
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.TestTrustStore
import paufregi.connectfeed.authToken
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.preAuthToken
import paufregi.connectfeed.sslSocketFactory
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.user
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class SyncWeightActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @JvmField @Rule(order = 1) val connectServer = MockWebServerRule(connectPort, sslSocketFactory, connectDispatcher)
    @JvmField @Rule(order = 1) val stravaServer = MockWebServerRule(stravaPort, sslSocketFactory, stravaDispatcher)

    @Inject
    lateinit var authStore: AuthStore

    @Inject
    lateinit var stravaStore: StravaStore

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    private val csvHeader =
        "No.,Date,Time,Weight(kg),BMI,Body Fat Percentage(%),Body Fat Mass(kg),Muscle Percentage(%),Muscle Mass(kg)," +
        "Skeletal Muscle Percentage(%),Skeletal Muscle Mass(kg),Bone Percentage(%),Bone Mass(kg),Protein Percentage(%)," +
        "Protein Mass(kg),Body Water Percentage(%),Body Water Mass(kg),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat," +
        "BMR(kcal),Metabolic Age,WHR (Waist-to-Hip Ratio),Optimal Weight(kg),Weight Level,Body Type," +
        "Target to optimal weight(kg),Target to optimal muscle mass(kg),Target to optimal fat mass(kg),Remarks,"

    private fun csvRow(date: String, time: String) =
        "1,$date,$time,74.15,23.1,14.1,10.46,81.6,60.51,55.5,41.15,4.3,3.18,19.6,14.53,62.0,45.97,63.69,12.3,6,1743,36,--,--,--,--,--,--,--,,"

    private fun csvFor(date: Date) =
        "$csvHeader\n${csvRow(SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH).format(date), "07:59:46")}"

    @Before
    fun setup() {
        Locale.setDefault(Locale.ENGLISH)
        TestTrustStore.install()
        FakeAndroidKeyStore.install()
        hiltRule.inject()
        runBlocking {
            authStore.saveUser(user)
            authStore.savePreAuthToken(preAuthToken)
            authStore.saveAuthToken(authToken)
        }
    }

    private fun shareIntent(content: String?): Intent {
        val intent = Intent(context, SyncWeightActivity::class.java).setAction(Intent.ACTION_SEND)
        content?.let {
            val uri = Uri.parse("content://paufregi.connectfeed.test/weights.csv")
            shadowOf(context.contentResolver).registerInputStream(uri, it.byteInputStream())
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.type = "application/vnd.ms-excel"
        }
        return intent
    }

    @Test
    fun `Sync weight - Garmin only`() = runEmptyComposeUiTest {
        ActivityScenario.launch<SyncWeightActivity>(shareIntent(csvFor(Date(0)))).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Sync succeeded").isDisplayed() }
        }

        assertThat(stravaServer.server.requestCount).isEqualTo(0)
        assertThat(connectServer.takeRequest().url.encodedPath).isEqualTo("/upload-service/upload")
    }

    @Test
    fun `Sync weight - with Strava`() = runEmptyComposeUiTest {
        runBlocking { stravaStore.saveToken(stravaAuthToken) }

        ActivityScenario.launch<SyncWeightActivity>(shareIntent(csvFor(Date()))).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Sync succeeded").isDisplayed() }
        }

        assertThat(stravaServer.server.requestCount).isGreaterThan(0)
        assertThat(connectServer.takeRequest().url.encodedPath).isEqualTo("/upload-service/upload")
    }

    @Test
    fun `Sync weight - Garmin failure`() = runEmptyComposeUiTest {
        connectServer.server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) = MockResponse(500)
        }

        ActivityScenario.launch<SyncWeightActivity>(shareIntent(csvFor(Date(0)))).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Garmin sync failed").isDisplayed() }
        }
    }

    @Test
    fun `Sync weight - Strava failure`() = runEmptyComposeUiTest {
        runBlocking { stravaStore.saveToken(stravaAuthToken) }
        stravaServer.server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) = MockResponse(500)
        }

        ActivityScenario.launch<SyncWeightActivity>(shareIntent(csvFor(Date()))).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Strava sync failed").isDisplayed() }
        }
    }

    @Test
    fun `Sync weight - invalid file`() = runEmptyComposeUiTest {
        ActivityScenario.launch<SyncWeightActivity>(shareIntent("not,a,renpho,file\n1,2,3,4")).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Failed to read file").isDisplayed() }
        }
    }

    @Test
    fun `Sync weight - empty file`() = runEmptyComposeUiTest {
        ActivityScenario.launch<SyncWeightActivity>(shareIntent(csvHeader)).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Nothing to sync").isDisplayed() }
        }
    }

    @Test
    fun `Sync weight - no file shared`() = runEmptyComposeUiTest {
        ActivityScenario.launch<SyncWeightActivity>(shareIntent(null)).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Don't know what to do").isDisplayed() }
        }
    }
}

