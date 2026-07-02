package paufregi.connectfeed.presentation.main

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
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
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

    @JvmField @Rule val connectServer = MockWebServerRule(connectPort, sslSocketFactory, connectDispatcher)
    @JvmField @Rule val garminSSOServer = MockWebServerRule(garminSSOPort, sslSocketFactory, garminSSODispatcher)
    @JvmField @Rule val strava = MockWebServerRule(stravaPort, sslSocketFactory, stravaDispatcher)
    @JvmField @Rule val github = MockWebServerRule(githubPort, sslSocketFactory, githubDispatcher)

    @Before
    fun setup() = runAndroidComposeUiTest<MainActivity> {
        hiltRule.inject()
    }

    @After
    fun tearDown() = runAndroidComposeUiTest<MainActivity> {
        database.close()
        runBlocking(Dispatchers.IO){
            authStore.dataStore.edit { it.clear() }
            stravaStore.dataStore.edit { it.clear() }
        }

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
        waitUntil(conditionDescription = "strava linked") { onNodeWithText("Strava linked").isDisplayed() }
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

        ActivityScenario.launch(MainActivity::class.java)
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

        ActivityScenario.launch(MainActivity::class.java)
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

        ActivityScenario.launch(MainActivity::class.java)
        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Strava activity").performClick()
        onNodeWithText("Bondcliff").performClick()
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

        ActivityScenario.launch(MainActivity::class.java)
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

        ActivityScenario.launch(MainActivity::class.java)
        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Edit").performClick()
        waitUntil(conditionDescription = "edit_screen") { onNodeWithTag("edit_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Strava activity").performClick()
        onNodeWithText("Bondcliff").performClick()
        onNodeWithText("Name").performTextInput("New Name")
        onNodeWithText("Event type").performClick()
        onNodeWithText("Race").performClick()
        onNodeWithText("Course").performClick()
        onNodeWithText("Course 2").performClick()
        onNodeWithText("Description").performTextInput("New Description")
        onNodeWithText("Water").performTextInput("50")
        onNodeWithTag("edit_screen").performTouchInput { swipeUp(centerY) }
        onNodeWithText("Training effect").performClick()
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Activity updated") { onNodeWithText("Activity updated").isDisplayed() }
    }

    @Test
    fun `Sync Strava activity`() = runAndroidComposeUiTest<MainActivity> {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)

        ActivityScenario.launch(MainActivity::class.java)
        awaitIdle()
        waitUntil(conditionDescription = "quick_edit_screen") { onNodeWithTag("quick_edit_screen").isDisplayed() }
        onNodeWithText("Sync Strava").performClick()
        waitUntil(conditionDescription = "sync_strava_screen") { onNodeWithTag("sync_strava_screen").isDisplayed() }
        onNodeWithText("Activity").performClick()
        onNodeWithText("Activity 1").performClick()
        onNodeWithText("Strava activity").performClick()
        onNodeWithText("Bondcliff").performClick()
        onNodeWithText("Save").performClick()

        waitUntil(conditionDescription = "Strava activity updated") { onNodeWithText("Strava activity updated").isDisplayed() }
    }
}