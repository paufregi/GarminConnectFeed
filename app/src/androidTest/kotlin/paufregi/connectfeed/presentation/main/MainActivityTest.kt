package paufregi.connectfeed.presentation.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
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
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

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
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
        runBlocking(Dispatchers.IO){
            authStore.dataStore.edit { it.clear() }
            stravaStore.dataStore.edit { it.clear() }
        }

    }

    @Test
    fun `Sign in`() = runTest {
        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.onNodeWithTag("login_screen").assertIsDisplayed()

        composeTestRule.onNodeWithText("Username").performTextInput("user")
        composeTestRule.onNodeWithText("Password").performTextInput("pass")
        composeTestRule.onNodeWithText("Sign in").performClick()
        composeTestRule.waitUntil(conditionDescription = "welcome") { composeTestRule.onNodeWithTag("welcome").isDisplayed() }
        composeTestRule.onNodeWithText("Ok").performClick()
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
    }

    @Test
    fun `Sign out`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.waitUntil(conditionDescription = "settings_screen") { composeTestRule.onNodeWithTag("settings_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Sign out").performClick()
        composeTestRule.waitUntil(conditionDescription = "sign_out_dialog") { composeTestRule.onNodeWithTag("sign_out_dialog").isDisplayed() }
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.waitUntil(conditionDescription = "login_screen") { composeTestRule.onNodeWithTag("login_screen").isDisplayed() }
    }

    @Test
    fun `Connect Strava`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.waitUntil(conditionDescription = "settings_screen") { composeTestRule.onNodeWithTag("settings_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Connect").performClick()
        composeTestRule.waitUntil(conditionDescription = "strava linked") { composeTestRule.onNodeWithText("Strava linked").isDisplayed() }
    }

    @Test
    fun `Disconnect Strava`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.waitUntil(conditionDescription = "settings_screen") { composeTestRule.onNodeWithTag("settings_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Remove").performClick()
        composeTestRule.waitUntil(conditionDescription = "strava_dialog") { composeTestRule.onNodeWithTag("strava_dialog").isDisplayed() }
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.waitUntil(conditionDescription = "settings_screen") { composeTestRule.onNodeWithTag("settings_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Connect").isDisplayed()
    }

    @Test
    fun `Refresh user`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.waitUntil(conditionDescription = "settings_screen") { composeTestRule.onNodeWithTag("settings_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Refresh").performClick()
        composeTestRule.waitUntil(conditionDescription = "User data refreshed") { composeTestRule.onNodeWithText("User data refreshed").isDisplayed() }
    }

    @Test
    fun `Create profile`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Profiles").performClick()
        composeTestRule.waitUntil(conditionDescription = "profiles_content") { composeTestRule.onNodeWithTag("profiles_content").isDisplayed() }
        composeTestRule.onNodeWithTag("create_profile").performClick()
        composeTestRule.waitUntil(conditionDescription = "profile_screen") { composeTestRule.onNodeWithTag("profile_screen").isDisplayed() }

        composeTestRule.onNodeWithText("Name").performTextInput("Profile 1")
        composeTestRule.onNodeWithText("Activity type").performClick()
        composeTestRule.onNodeWithText("Running").performClick()
        composeTestRule.onNodeWithText("Event type").performClick()
        composeTestRule.onNodeWithText("Race").performClick()
        composeTestRule.onNodeWithText("Course").performClick()
        composeTestRule.onNodeWithText("Course 1").performClick()
        composeTestRule.onNodeWithText("Water").performTextInput("500")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.waitUntil(conditionDescription = "Profile saved") { composeTestRule.onNodeWithText("Profile saved").isDisplayed() }
    }

    @Test
    fun `Update profile`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        dao.saveProfile(ProfileEntity(id = 5, userId = user.id, name = "Profile 1", activityType = ActivityType.Running, eventType = EventType.Race))

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Profiles").performClick()
        composeTestRule.waitUntil(conditionDescription = "profiles_content") { composeTestRule.onNodeWithTag("profiles_content").isDisplayed() }
        composeTestRule.onNodeWithText("Profile 1").performClick()
        composeTestRule.waitUntil(conditionDescription = "profile_screen") { composeTestRule.onNodeWithTag("profile_screen").isDisplayed() }

        composeTestRule.onNodeWithText("Name").performTextClearance()
        composeTestRule.onNodeWithText("Name").performTextInput("Profile 2")
        composeTestRule.onNodeWithText("Activity type").performClick()
        composeTestRule.onNodeWithText("Cycling").performClick()
        composeTestRule.onNodeWithText("Event type").performClick()
        composeTestRule.onNodeWithText("Training").performClick()
        composeTestRule.onNodeWithText("Course").performClick()
        composeTestRule.onNodeWithText("Course 2").performClick()
        composeTestRule.onNodeWithText("Water").performTextInput("100")
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.waitUntil(conditionDescription = "Profile saved") { composeTestRule.onNodeWithText("Profile saved").isDisplayed() }
    }

    @Test
    fun `Delete profile`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        dao.saveProfile(ProfileEntity(id = 10, userId = user.id, name = "Profile 1", activityType = ActivityType.Running, eventType = EventType.Race))

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Profiles").performClick()
        composeTestRule.waitUntil(conditionDescription = "profiles_content") { composeTestRule.onNodeWithTag("profiles_content").isDisplayed() }
        composeTestRule.onNodeWithTag("delete_profile_10").performClick()

        composeTestRule.onNodeWithText("Profile 1").isNotDisplayed()
    }

    @Test
    fun `Quick edit activity`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        dao.saveProfile(ProfileEntity(userId = user.id, name = "Profile 1", activityType = ActivityType.Cycling, eventType = EventType.Race))

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Activity").performClick()
        composeTestRule.onNodeWithText("Activity 1").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Profile 1").performClick()
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.waitUntil(conditionDescription = "Activity updated") { composeTestRule.onNodeWithText("Activity updated").isDisplayed() }
    }

    @Test
    fun `Quick edit - with Strava`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)
        dao.saveProfile(ProfileEntity(userId = user.id, name = "Profile 1", activityType = ActivityType.Cycling, eventType = EventType.Race))

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Activity").performClick()
        composeTestRule.onNodeWithText("Activity 1").performClick()
        composeTestRule.onNodeWithText("Strava activity").performClick()
        composeTestRule.onNodeWithText("Bondcliff").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Profile 1").performClick()
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.waitUntil(conditionDescription = "Activity updated") { composeTestRule.onNodeWithText("Activity updated").isDisplayed() }
    }

    @Test
    fun `Edit activity`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)

        ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun `Edit - with Strava`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)

        ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun `Sync Strava activity`() = runTest {
        authStore.saveUser(user)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaAuthToken)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_screen") { composeTestRule.onNodeWithTag("quick_edit_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Sync Strava").performClick()
        composeTestRule.waitUntil(conditionDescription = "sync_strava_screen") { composeTestRule.onNodeWithTag("sync_strava_screen").isDisplayed() }
        composeTestRule.onNodeWithText("Activity").performClick()
        composeTestRule.onNodeWithText("Activity 1").performClick()
        composeTestRule.onNodeWithText("Strava activity").performClick()
        composeTestRule.onNodeWithText("Bondcliff").performClick()
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.waitUntil(conditionDescription = "Strava activity updated") { composeTestRule.onNodeWithText("Strava activity updated").isDisplayed() }
    }
}