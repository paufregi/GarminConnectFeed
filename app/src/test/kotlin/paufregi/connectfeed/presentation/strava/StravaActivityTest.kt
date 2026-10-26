package paufregi.connectfeed.presentation.strava

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import paufregi.connectfeed.FakeAndroidKeyStore
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.TestTrustStore
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.getFields
import paufregi.connectfeed.sslSocketFactory
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class StravaActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @JvmField @Rule(order = 1) val stravaServer = MockWebServerRule(stravaPort, sslSocketFactory, stravaDispatcher)

    @Inject
    lateinit var stravaStore: StravaStore

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    @Before
    fun setup() {
        TestTrustStore.install()
        FakeAndroidKeyStore.install()
        hiltRule.inject()
    }

    private fun callbackIntent(code: String?): Intent {
        val uri = Uri.parse("paufregi.connectfeed://strava/auth").buildUpon()
            .apply { code?.let { appendQueryParameter("code", it) } }
            .build()
        return Intent(Intent.ACTION_VIEW, uri, context, StravaActivity::class.java)
    }

    @Test
    fun `Link Strava - success`() = runEmptyComposeUiTest {
        ActivityScenario.launch<StravaActivity>(callbackIntent("123456")).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Strava linked").isDisplayed() }
        }

        val request = stravaServer.takeRequest()
        assertThat(request.url.encodedPath).isEqualTo("/api/v3/oauth/token")
        assertThat(request.getFields()["code"]).isEqualTo("123456")
        assertThat(request.getFields()["grant_type"]).isEqualTo("authorization_code")

        assertThat(runBlocking { stravaStore.getToken().first() }).isEqualTo(stravaAuthToken)
    }

    @Test
    fun `Link Strava - failure`() = runEmptyComposeUiTest {
        stravaServer.server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) = MockResponse(401)
        }

        ActivityScenario.launch<StravaActivity>(callbackIntent("123456")).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Link failed").isDisplayed() }
        }

        assertThat(runBlocking { stravaStore.getToken().first() }).isNull()
    }

    @Test
    fun `Link Strava - no code`() = runEmptyComposeUiTest {
        ActivityScenario.launch<StravaActivity>(callbackIntent(null)).use {
            waitUntil(timeoutMillis = 10_000) { onNodeWithText("Don't know what to do").isDisplayed() }
        }

        assertThat(stravaServer.server.requestCount).isEqualTo(0)
    }
}

