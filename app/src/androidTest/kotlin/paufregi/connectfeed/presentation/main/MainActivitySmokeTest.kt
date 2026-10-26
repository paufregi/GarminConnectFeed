package paufregi.connectfeed.presentation.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.authToken
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.database.GarminDatabase
import paufregi.connectfeed.data.database.entities.ProfileEntity
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.githubDispatcher
import paufregi.connectfeed.githubPort
import paufregi.connectfeed.preAuthToken
import paufregi.connectfeed.sslSocketFactory
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.user
import javax.inject.Inject

/**
 * On-device end-to-end smoke tests.
 *
 * Only the critical journeys run here; the complete flow suite (profiles, settings,
 * Strava disconnect, etc.) lives in the Robolectric `MainActivityTest` under `src/test`.
 * "Connect Strava" stays on-device because it depends on the system browser following
 * the OAuth redirect back into the app via deep link.
 */
@HiltAndroidTest
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class MainActivitySmokeTest {

    private companion object {
        // Flows involve several TLS round trips on a cold emulator
        const val TIMEOUT = 10_000L
    }

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authStore: AuthStore

    @Inject
    lateinit var stravaStore: StravaStore

    @Inject
    lateinit var database: GarminDatabase

    @Inject
    lateinit var dao: GarminDao

    @JvmField @Rule(order = 1) val connectServer = MockWebServerRule(connectPort, sslSocketFactory, connectDispatcher)
    @JvmField @Rule(order = 1) val garminSSOServer = MockWebServerRule(garminSSOPort, sslSocketFactory, garminSSODispatcher)
    @JvmField @Rule(order = 1) val strava = MockWebServerRule(stravaPort, sslSocketFactory, stravaDispatcher)
    @JvmField @Rule(order = 1) val github = MockWebServerRule(githubPort, sslSocketFactory, githubDispatcher)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Sign in`() = runAndroidComposeUiTest<MainActivity> {
        onNodeWithTag("login_screen").assertIsDisplayed()

        onNodeWithText("Username").performTextInput("user")
        onNodeWithText("Password").performTextInput("pass")
        onNodeWithText("Sign in").performClick()
        waitUntil(conditionDescription = "quick_edit_screen", timeoutMillis = TIMEOUT) { onNodeWithTag("quick_edit_screen").isDisplayed() }
    }

    @Test
    fun `Connect Strava`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen", timeoutMillis = TIMEOUT) { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithTag("menu").performClick()
        onNodeWithText("Settings").performClick()
        waitUntil(conditionDescription = "settings_screen", timeoutMillis = TIMEOUT) { onNodeWithTag("settings_screen").isDisplayed() }
        onNodeWithText("Connect").performClick()
        waitUntil(conditionDescription = "strava linked", timeoutMillis = TIMEOUT) { onNodeWithText("Strava linked").isDisplayed() }
    }

    @Test
    fun `Quick edit activity - with Strava`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)
        dao.saveProfile(ProfileEntity(userId = user.id, name = "Profile 1", type = ActivityType.Cycling, eventType = EventType.Race))

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen", timeoutMillis = TIMEOUT) { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Profile").performClick()
        onNodeWithText("Profile 1").performClick()
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Activity updated", timeoutMillis = TIMEOUT) { onNodeWithText("Activity updated").isDisplayed() }
    }

    @Test
    fun `Edit activity`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen", timeoutMillis = TIMEOUT) { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Edit").performClick()
        waitUntil(conditionDescription = "edit_screen", timeoutMillis = TIMEOUT) { onNodeWithTag("edit_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Name").performTextInput("New Name")
        onNodeWithText("Event type").performClick()
        onNodeWithText("Race").performClick()
        onNodeWithText("Course").performClick()
        onNodeWithText("Course 2").performClick()
        onNodeWithText("Water").performTextInput("50")
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Activity updated", timeoutMillis = TIMEOUT) { onNodeWithText("Activity updated").isDisplayed() }
    }
}

