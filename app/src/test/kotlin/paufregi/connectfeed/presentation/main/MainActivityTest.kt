package paufregi.connectfeed.presentation.main

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import javax.inject.Named

@HiltAndroidTest
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class MainActivityTest {

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

    @Inject
    @Named("StravaAuthUri")
    lateinit var stravaAuthUri: Uri

    @JvmField @Rule(order = 1) val connectServer = MockWebServerRule(connectPort, sslSocketFactory, connectDispatcher)
    @JvmField @Rule(order = 1) val garminSSOServer = MockWebServerRule(garminSSOPort, sslSocketFactory, garminSSODispatcher)
    @JvmField @Rule(order = 1) val strava = MockWebServerRule(stravaPort, sslSocketFactory, stravaDispatcher)
    @JvmField @Rule(order = 1) val github = MockWebServerRule(githubPort, sslSocketFactory, githubDispatcher)

    @Before
    fun setup() {
        FakeAndroidKeyStore.install()
        TestTrustStore.install()
        hiltRule.inject()
    }

    @Test
    fun `Sign in`() = runAndroidComposeUiTest<MainActivity> {
        onNodeWithTag("login_screen").assertIsDisplayed()

        onNodeWithText("Username").performTextInput("user")
        onNodeWithText("Password").performTextInput("pass")
        onNodeWithText("Sign in").performClick()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
    }

    @Test
    fun `Sign out`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithTag("menu").performClick()
        onNodeWithText("Settings").performClick()
        waitUntil(conditionDescription = "settings_screen") { onNodeWithTag("settings_screen").isDisplayed() }
        onNodeWithText("Sign out").performClick()
        waitUntil(conditionDescription = "sign_out_dialog") { onNodeWithTag("sign_out_dialog").isDisplayed() }
        onNodeWithText("Confirm").performClick()
        waitUntil(conditionDescription = "login_screen") { onNodeWithTag("login_screen").isDisplayed() }
    }

    @Test
    fun `Connect Strava`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithTag("menu").performClick()
        onNodeWithText("Settings").performClick()
        waitUntil(conditionDescription = "settings_screen") { onNodeWithTag("settings_screen").isDisplayed() }
        onNodeWithText("Connect").performClick()

        // No browser under Robolectric: verify the OAuth intent was fired.
        // The deep-link callback is covered end-to-end by StravaActivityTest.
        val intent = shadowOf(ApplicationProvider.getApplicationContext<Application>()).nextStartedActivity
        assertThat(intent.action).isEqualTo(Intent.ACTION_VIEW)
        assertThat(intent.data).isEqualTo(stravaAuthUri)
    }

    @Test
    fun `Disconnect Strava`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithTag("menu").performClick()
        onNodeWithText("Settings").performClick()
        waitUntil(conditionDescription = "settings_screen") { onNodeWithTag("settings_screen").isDisplayed() }
        onNodeWithText("Remove").performClick()
        waitUntil(conditionDescription = "strava_dialog") { onNodeWithTag("strava_dialog").isDisplayed() }
        onNodeWithText("Confirm").performClick()
        waitUntil(conditionDescription = "settings_screen") { onNodeWithTag("settings_screen").isDisplayed() }
        onNodeWithText("Connect").assertIsDisplayed()
    }

    @Test
    fun `Refresh user`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithTag("menu").performClick()
        onNodeWithText("Settings").performClick()
        waitUntil(conditionDescription = "settings_screen") { onNodeWithTag("settings_screen").isDisplayed() }
        onNodeWithText("Refresh").performClick()
        waitUntil(conditionDescription = "User data refreshed") { onNodeWithText("User data refreshed").isDisplayed() }
    }

    @Test
    fun `Create profile`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithTag("menu").performClick()
        onNodeWithText("Profiles").performClick()
        waitUntil(conditionDescription = "profiles_content") { onNodeWithTag("profiles_content").isDisplayed() }
        onNodeWithTag("create_profile").performClick()
        waitUntil(conditionDescription = "profile_screen") { onNodeWithTag("profile_screen").isDisplayed() }

        onNodeWithText("Name").performTextInput("Profile 1")
        onNodeWithText("Type").performClick()
        onNodeWithText("Running").performClick()
        onNodeWithText("Event type").performClick()
        onNodeWithText("Race").performClick()
        onNodeWithText("Course").performClick()
        onNodeWithText("Course 1").performClick()
        onNodeWithText("Water").performTextInput("500")
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Profile saved") { onNodeWithText("Profile saved").isDisplayed() }
    }

    @Test
    fun `Update profile`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        dao.saveProfile(ProfileEntity(id = 5, userId = user.id, name = "Profile 1", type = ActivityType.Running, eventType = EventType.Race))

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithTag("menu").performClick()
        onNodeWithText("Profiles").performClick()
        waitUntil(conditionDescription = "profiles_content") { onNodeWithTag("profiles_content").isDisplayed() }
        onNodeWithText("Profile 1").performClick()
        waitUntil(conditionDescription = "profile_screen") { onNodeWithTag("profile_screen").isDisplayed() }

        onNodeWithText("Name").performTextClearance()
        onNodeWithText("Name").performTextInput("Profile 2")
        onNodeWithText("Type").performClick()
        onNodeWithText("Cycling").performClick()
        onNodeWithText("Event type").performClick()
        onNodeWithText("Training").performClick()
        onNodeWithText("Course").performClick()
        onNodeWithText("Course 2").performClick()
        onNodeWithText("Water").performTextInput("100")
        onNodeWithText("Save").assertIsEnabled()
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Profile saved") { onNodeWithText("Profile saved").isDisplayed() }
    }

    @Test
    fun `Delete profile`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        dao.saveProfile(ProfileEntity(id = 10, userId = user.id, name = "Profile 1", type = ActivityType.Running, eventType = EventType.Race))

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithTag("menu").performClick()
        onNodeWithText("Profiles").performClick()
        waitUntil(conditionDescription = "profiles_content") { onNodeWithTag("profiles_content").isDisplayed() }
        onNodeWithTag("delete_profile_10").performClick()
        waitUntil(conditionDescription = "delete_dialog") { onNodeWithTag("delete_dialog").isDisplayed() }
        onNodeWithText("Confirm").performClick()
        waitUntil(conditionDescription = "profiles_content") { onNodeWithTag("profiles_content").isDisplayed() }

        onNodeWithText("Profile 1").assertIsNotDisplayed()
    }

    @Test
    fun `Quick edit activity`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        dao.saveProfile(ProfileEntity(userId = user.id, name = "Profile 1", type = ActivityType.Cycling, eventType = EventType.Race))

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Profile").performClick()
        onNodeWithText("Profile 1").performClick()
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Activity updated") { onNodeWithText("Activity updated").isDisplayed() }
    }

    @Test
    fun `Quick edit - with Strava`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)
        dao.saveProfile(ProfileEntity(userId = user.id, name = "Profile 1", type = ActivityType.Cycling, eventType = EventType.Race))

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Profile").performClick()
        onNodeWithText("Profile 1").performClick()
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Activity updated") { onNodeWithText("Activity updated").isDisplayed() }
    }

    @Test
    fun `Edit activity`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Edit").performClick()
        waitUntil(conditionDescription = "edit_screen") { onNodeWithTag("edit_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Name").performTextInput("New Name")
        onNodeWithText("Event type").performClick()
        onNodeWithText("Race").performClick()
        onNodeWithText("Course").performClick()
        onNodeWithText("Course 2").performClick()
        onNodeWithText("Water").performTextInput("50")
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Activity updated") { onNodeWithText("Activity updated").isDisplayed() }
    }

    @Test
    fun `Edit - with Strava`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)

        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Edit").performClick()
        waitUntil(conditionDescription = "edit_screen") { onNodeWithTag("edit_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Name").performTextInput("New Name")
        onNodeWithText("Event type").performClick()
        onNodeWithText("Race").performClick()
        onNodeWithText("Course").performClick()
        onNodeWithText("Course 2").performClick()
        waitUntil(conditionDescription = "edit_screen", timeoutMillis = 100000) { onNodeWithText("Description").isDisplayed() }
        onNodeWithText("Description").performTextInput("New Description")
        onNodeWithText("Water").performTextInput("50")
        onNodeWithTag("edit_screen").performTouchInput { swipeUp(centerY) }
        onNodeWithText("Training effect").performClick()
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Activity updated") { onNodeWithText("Activity updated").isDisplayed() }
    }
}