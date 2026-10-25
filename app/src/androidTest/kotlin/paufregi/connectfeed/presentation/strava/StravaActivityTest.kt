package paufregi.connectfeed.presentation.strava

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
import kotlinx.coroutines.flow.first
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
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.user
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalTestApi
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class StravaActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Rule @JvmField val stravaServer = MockServer.createSecure(stravaPort, stravaDispatcher)

    @Inject
    lateinit var authStore: AuthStore

    private val failingDispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest) = MockResponse(code = 400, body = "invalid code")
    }

    private fun stravaCallback(code: String? = "123456"): Intent {
        val uri = "paufregi.connectfeed://strava/auth".toUri().buildUpon()
            .apply { code?.let { appendQueryParameter("code", it) } }
            .build()
        return Intent(Intent.ACTION_VIEW, uri)
            .setClass(ApplicationProvider.getApplicationContext(), StravaActivity::class.java)
    }

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
        runBlocking { authStore.clear() }
    }

    @Test
    fun `Link Strava`() = runEmptyComposeUiTest {
        ActivityScenario.launch<StravaActivity>(stravaCallback()).use {
            waitUntil(conditionDescription = "Strava linked") {
                onNodeWithText("Strava linked!").isDisplayed()
            }
            val token = runBlocking { authStore.stravaToken.first() }
            assertThat(token?.accessToken).isEqualTo(stravaAuthToken.accessToken)
            assertThat(token?.refreshToken).isEqualTo(stravaAuthToken.refreshToken)
        }
    }

    @Test
    fun `Link Strava - exchange fails`() = runEmptyComposeUiTest {
        stravaServer.server.dispatcher = failingDispatcher

        ActivityScenario.launch<StravaActivity>(stravaCallback()).use {
            waitUntil(conditionDescription = "link failure") {
                onNodeWithText("Link failed!").isDisplayed()
            }
            onNodeWithText("Something went wrong: invalid code").isDisplayed()
            val token = runBlocking { authStore.stravaToken.first() }
            assertThat(token).isNull()
        }
    }

    @Test
    fun `Link Strava - missing code keeps loading`() = runEmptyComposeUiTest {
        ActivityScenario.launch<StravaActivity>(stravaCallback(code = null)).use {
            waitUntil(conditionDescription = "loading") {
                onNodeWithText("Connect Strava").isDisplayed()
            }
            val token = runBlocking { authStore.stravaToken.first() }
            assertThat(token).isNull()
        }
    }
}

