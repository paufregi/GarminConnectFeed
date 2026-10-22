package paufregi.connectfeed.presentation.app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.MockServer
import paufregi.connectfeed.authToken
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.database.GarminDatabase
import paufregi.connectfeed.data.database.entities.ProfileEntity
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.garminAuthDispatcher
import paufregi.connectfeed.garminAuthPort
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.user
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class AppActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @JvmField
    @Rule
    val connectServer = MockServer.createSecure(connectPort, connectDispatcher)

    @JvmField
    @Rule
    val garminAuthServer = MockServer.createSecure(garminAuthPort, garminAuthDispatcher)

    @JvmField
    @Rule
    val garminSSOServer = MockServer.createSecure(garminSSOPort, garminSSODispatcher)

    @JvmField
    @Rule
    val stravaServer = MockServer.createSecure(stravaPort, stravaDispatcher)

    @Inject
    lateinit var authStore: AuthStore

    @Inject
    lateinit var database: GarminDatabase

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking {
            authStore.clear()
            database.clearAllTables()
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            authStore.clear()
            database.clearAllTables()
        }
    }

    @Test
    fun `Sign in`() = runAndroidComposeUiTest<AppActivity> {
        onNodeWithText("Username").performTextInput("user")
        onNodeWithText("Password").performTextInput("pass")
        onNodeWithText("Sign in").performClick()

        waitUntil(conditionDescription = "Garmin welcome screen") {
            onNodeWithText("Welcome Paul").isDisplayed()
        }
        onNodeWithText("OK").performClick()
        waitUntil(conditionDescription = "activities screen") {
            onNodeWithTag("activity_list").isDisplayed()
        }
    }

    @Test
    fun `Sign out`() = runAndroidComposeUiTest<AppActivity> {
        authenticate()
        waitForActivities()
        openDestination("Settings")

        onNodeWithText("Sign out").performClick()
        waitUntil(conditionDescription = "sign out confirmation") {
            onNodeWithTag("sign_out_dialog").isDisplayed()
        }
        onNodeWithText("Confirm").performClick()
        waitUntil(conditionDescription = "login screen") {
            onNodeWithText("Username").isDisplayed()
        }
    }

    @Test
    fun `Connect Strava`() = runAndroidComposeUiTest<AppActivity> {
        authenticate()
        waitForActivities()
        openDestination("Settings")

        onNodeWithText("Connect").performClick()
        waitUntil(conditionDescription = "Strava linked") {
            onNodeWithText("Strava linked", substring = true).isDisplayed()
        }
    }

    @Test
    fun `Disconnect Strava`() = runAndroidComposeUiTest<AppActivity> {
        authenticate(withStrava = true)
        waitForActivities()
        openDestination("Settings")

        onNodeWithText("Remove").performClick()
        waitUntil(conditionDescription = "Strava disconnect confirmation") {
            onNodeWithTag("strava_dialog").isDisplayed()
        }
        onNodeWithText("Confirm").performClick()
        waitUntil(conditionDescription = "Strava disconnected") {
            onNodeWithText("Connect").isDisplayed()
        }
    }

    @Test
    fun `Refresh user`() = runAndroidComposeUiTest<AppActivity> {
        authenticate()
        waitForActivities()
        openDestination("Settings")

        onNodeWithText("Refresh").performClick()
        waitUntil(conditionDescription = "user data refreshed") {
            onNodeWithText("User data refreshed").isDisplayed()
        }
    }

    @Test
    fun `Manage profiles`() = runAndroidComposeUiTest<AppActivity> {
        authenticate()
        waitForActivities()
        openDestination("Profiles")

        onNodeWithTag("create_profile").performClick()
        onNodeWithText("Name").performTextInput("Profile 1")
        onNodeWithText("Type").performClick()
        onNodeWithText("Running").performClick()
        onNodeWithText("Event type").performClick()
        onNodeWithText("Race").performClick()
        onNodeWithText("Save").performClick()
        waitUntil(conditionDescription = "profile saved") {
            onNodeWithText("Profile saved").isDisplayed()
        }
        val profileId = database.garminDao().getAllProfiles(user.id).first().single().id

        onNodeWithText("Profile 1").performClick()
        onNodeWithText("Name").performTextClearance()
        onNodeWithText("Name").performTextInput("Updated profile")
        onNodeWithText("Save").performClick()
        waitUntil(conditionDescription = "updated profile") {
            onNodeWithText("Updated profile").isDisplayed()
        }

        onNodeWithTag("delete_profile_$profileId").performClick()
        waitUntil(conditionDescription = "profile deleted") {
            onNodeWithText("Profile deleted").isDisplayed()
        }
        waitUntil(conditionDescription = "empty profiles list") {
            onNodeWithText("No profiles").isDisplayed()
        }
    }

    @Test
    fun `Manage gears`() = runAndroidComposeUiTest<AppActivity> {
        authenticate()
        waitForActivities()
        openDestination("Gears")

        onNodeWithTag("sync_gears").performClick()
        waitUntil(conditionDescription = "gears synced") {
            onNodeWithText("Gears synced").isDisplayed()
        }
        onNodeWithText("Mizuno Neo Vista").assertIsDisplayed()
        onNodeWithText("Nova").assertIsDisplayed()
    }

    @Test
    fun `Quick edit activity without Strava`() = runAndroidComposeUiTest<AppActivity> {
        authenticate()
        database.garminDao().saveProfile(
            ProfileEntity(
                userId = user.id,
                name = "Profile 1",
                type = ActivityType.Cycling,
                eventType = EventType.Race,
            )
        )
        waitForActivities()

        openActivity()
        onNodeWithText("Profile 1").performClick()
        onNodeWithText("Save").performClick()
        waitUntil(conditionDescription = "activity quick-edited") {
            onNodeWithText("Update completed!").isDisplayed()
        }
    }

    @Test
    fun `Quick edit activity with Strava`() = runAndroidComposeUiTest<AppActivity> {
        authenticate(withStrava = true)
        database.garminDao().saveProfile(
            ProfileEntity(
                userId = user.id,
                name = "Profile 1",
                type = ActivityType.Cycling,
                eventType = EventType.Race,
            )
        )
        waitForActivities()

        openActivity()
        onNodeWithText("Profile 1").performClick()
        onNodeWithText("Description").assertIsDisplayed()
        onNodeWithText("Description").performTextInput("Smoke test description")
        onNodeWithText("Save").performClick()
        waitUntil(conditionDescription = "Strava activity quick-edited") {
            onNodeWithText("Update completed!").isDisplayed()
        }
    }

    @Test
    fun `Manual edit activity without Strava`() = runAndroidComposeUiTest<AppActivity> {
        authenticate()
        waitForActivities()

        openActivity()
        onNodeWithText("Edit").performClick()
        onNodeWithText("Name").performTextClearance()
        onNodeWithText("Name").performTextInput("Updated activity")
        onNodeWithText("Save").performClick()
        waitUntil(conditionDescription = "activity manually edited") {
            onNodeWithText("Update completed!").isDisplayed()
        }
    }

    @Test
    fun `Manual edit activity with Strava`() = runAndroidComposeUiTest<AppActivity> {
        authenticate(withStrava = true)
        waitForActivities()

        openActivity()
        onNodeWithText("Edit").performClick()
        onNodeWithText("Name").performTextClearance()
        onNodeWithText("Name").performTextInput("Updated activity")
        onNodeWithText("Description").performScrollTo()
        onNodeWithText("Description").performTextInput("Smoke test description")
        onNodeWithText("Save").performClick()
        waitUntil(conditionDescription = "Strava activity manually edited") {
            onNodeWithText("Update completed!").isDisplayed()
        }
    }

    private suspend fun authenticate(withStrava: Boolean = false) {
        authStore.saveUser(user)
        authStore.saveGarminToken(authToken)
        if (withStrava) authStore.saveStravaToken(stravaAuthToken)
    }

    private fun AndroidComposeUiTest<AppActivity>.waitForActivities() {
        waitUntil(conditionDescription = "activities list") {
            onNodeWithTag("activity_list").isDisplayed()
        }
    }

    private fun AndroidComposeUiTest<AppActivity>.openDestination(destination: String) {
        onNodeWithTag("menu").performClick()
        onNodeWithText(destination).performClick()
    }

    private fun AndroidComposeUiTest<AppActivity>.openActivity() {
        onNodeWithText("Activity 1").performClick()
    }
}
