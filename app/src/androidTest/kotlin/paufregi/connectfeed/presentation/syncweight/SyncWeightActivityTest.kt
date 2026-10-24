package paufregi.connectfeed.presentation.syncweight

import android.content.Context
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runEmptyComposeUiTest
import androidx.core.net.toUri
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.MockServer
import paufregi.connectfeed.authToken
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.user
import java.io.File
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalTestApi
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class SyncWeightActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Rule @JvmField val connectServer = MockServer.createSecure(connectPort, connectDispatcher)

    @Inject
    lateinit var authStore: AuthStore

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    private val failingDispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest) = MockResponse(code = 500, body = "upload failed")
    }

    private val renphoHeader =
        "No.,Date,Time,Weight(kg),BMI,Body Fat Percentage(%),Body Fat Mass(kg),Muscle Percentage(%),Muscle Mass(kg)," +
        "Skeletal Muscle Percentage(%),Skeletal Muscle Mass(kg),Bone Percentage(%),Bone Mass(kg),Protein Percentage(%)," +
        "Protein Mass(kg),Body Water Percentage(%),Body Water Mass(kg),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat," +
        "BMR(kcal),Metabolic Age,WHR (Waist-to-Hip Ratio),Optimal Weight(kg),Weight Level,Body Type," +
        "Target to optimal weight(kg),Target to optimal muscle mass(kg),Target to optimal fat mass(kg),Remarks,"

    private val renphoRow =
        "1,2026.08.16,07:59:46,74.15,23.1,14.1,10.46,81.6,60.51,55.5,41.15,4.3,3.18,19.6,14.53,62.0,45.97,63.69,12.3,6,1743,36,--,--,--,--,--,--,--,,"

    private val files = mutableListOf<File>()

    private fun csvFile(vararg lines: String): File =
        File.createTempFile("renpho", ".csv", context.cacheDir)
            .apply { writeText(lines.joinToString("\n")) }
            .also { files.add(it) }

    private fun shareIntent(file: File?): Intent =
        Intent(Intent.ACTION_SEND)
            .setClass(context, SyncWeightActivity::class.java)
            .apply { file?.let { putExtra(Intent.EXTRA_STREAM, it.toUri()) } }

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking {
            authStore.saveUser(user)
            authStore.saveGarminToken(authToken)
        }
    }

    @After
    fun tearDown() {
        files.forEach { it.delete() }
        runBlocking { authStore.clear() }
    }

    @Test
    fun `Sync weight`() = runEmptyComposeUiTest {
        val file = csvFile(renphoHeader, renphoRow)

        ActivityScenario.launch<SyncWeightActivity>(shareIntent(file)).use {
            waitUntil(conditionDescription = "sync succeeded") {
                onNodeWithText("Sync succeeded").isDisplayed()
            }
        }

        val upload = connectServer.server.takeRequest()
        assertThat(upload.url.encodedPath).isEqualTo("/upload-service/upload")
        assertThat(upload.method).isEqualTo("POST")
    }

    @Test
    fun `Sync weight - upload fails`() = runEmptyComposeUiTest {
        connectServer.server.dispatcher = failingDispatcher
        val file = csvFile(renphoHeader, renphoRow)

        ActivityScenario.launch<SyncWeightActivity>(shareIntent(file)).use {
            waitUntil(conditionDescription = "sync failed") {
                onNodeWithText("Sync failed!").isDisplayed()
            }
            assertThat(onNodeWithText("Something went wrong: Couldn't update Garmin: upload failed").isDisplayed()).isTrue()
        }
    }

    @Test
    fun `Sync weight - empty file`() = runEmptyComposeUiTest {
        val file = csvFile(renphoHeader)

        ActivityScenario.launch<SyncWeightActivity>(shareIntent(file)).use {
            waitUntil(conditionDescription = "nothing to sync") {
                onNodeWithText("Something went wrong: nothing to sync").isDisplayed()
            }
        }
    }

    @Test
    fun `Sync weight - unreadable file`() = runEmptyComposeUiTest {
        val file = csvFile("not,a,renpho,file", "a,b,c,d")

        ActivityScenario.launch<SyncWeightActivity>(shareIntent(file)).use {
            waitUntil(conditionDescription = "failed to read") {
                onNodeWithText("Something went wrong: failed to read file").isDisplayed()
            }
        }
    }
}

