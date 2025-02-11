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
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.consumer
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.database.GarminDatabase
import paufregi.connectfeed.data.database.entities.ProfileEntity
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.garthDispatcher
import paufregi.connectfeed.garthPort
import paufregi.connectfeed.oauth1
import paufregi.connectfeed.oauth2
import paufregi.connectfeed.sslSocketFactory
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
    lateinit var database: GarminDatabase

    @Inject
    lateinit var dao: GarminDao

    private val connectServer = MockWebServer()
    private val garminSSOServer = MockWebServer()
    private val garthServer = MockWebServer()

    @Before
    fun setup() {
        hiltRule.inject()
        connectServer.useHttps(sslSocketFactory, false)
        connectServer.start(connectPort)
        garminSSOServer.useHttps(sslSocketFactory, false)
        garminSSOServer.start(garminSSOPort)
        garthServer.useHttps(sslSocketFactory, false)
        garthServer.start(garthPort)

        connectServer.dispatcher = connectDispatcher
        garthServer.dispatcher = garthDispatcher
        garminSSOServer.dispatcher = garminSSODispatcher
    }

    @After
    fun tearDown() {
        connectServer.shutdown()
        garminSSOServer.shutdown()
        garthServer.shutdown()
        database.close()
        runBlocking(Dispatchers.IO){
            authStore.dataStore.edit { it.clear() }
        }

    }

    @Test
    fun `Sign in`() = runTest {
        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.onNodeWithTag("login_form").assertIsDisplayed()

        composeTestRule.onNodeWithText("Username").performTextInput("user")
        composeTestRule.onNodeWithText("Password").performTextInput("pass")
        composeTestRule.onNodeWithText("Sign in").performClick()
        composeTestRule.waitUntil(conditionDescription = "welcome") { composeTestRule.onNodeWithTag("welcome").isDisplayed() }
        composeTestRule.onNodeWithText("Ok").performClick()
        composeTestRule.waitUntil(conditionDescription = "quick_edit_form") { composeTestRule.onNodeWithTag("quick_edit_form").isDisplayed() }
    }

    @Test
    fun `Sign out`() = runTest {
        authStore.saveUser(user)
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_form") { composeTestRule.onNodeWithTag("quick_edit_form").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Account").performClick()
        composeTestRule.waitUntil(conditionDescription = "account_form") { composeTestRule.onNodeWithTag("account_form").isDisplayed() }
        composeTestRule.onNodeWithText("Sign out").performClick()
        composeTestRule.waitUntil(conditionDescription = "sign_out_dialog") { composeTestRule.onNodeWithTag("sign_out_dialog").isDisplayed() }
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.waitUntil(conditionDescription = "login_form") { composeTestRule.onNodeWithTag("login_form").isDisplayed() }
    }

    @Test
    fun `Refresh user`() = runTest {
        authStore.saveUser(user)
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_form") { composeTestRule.onNodeWithTag("quick_edit_form").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Account").performClick()
        composeTestRule.waitUntil(conditionDescription = "account_form") { composeTestRule.onNodeWithTag("account_form").isDisplayed() }
        composeTestRule.onNodeWithText("Refresh user").performClick()
        composeTestRule.waitUntil(conditionDescription = "User data refreshed") { composeTestRule.onNodeWithText("User data refreshed").isDisplayed() }
    }

    @Test
    fun `Create profile`() = runTest {
        authStore.saveUser(user)
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_form") { composeTestRule.onNodeWithTag("quick_edit_form").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Profiles").performClick()
        composeTestRule.waitUntil(conditionDescription = "profiles_content") { composeTestRule.onNodeWithTag("profiles_content").isDisplayed() }
        composeTestRule.onNodeWithTag("create_profile").performClick()
        composeTestRule.waitUntil(conditionDescription = "profile_form") { composeTestRule.onNodeWithTag("profile_form").isDisplayed() }

        composeTestRule.onNodeWithText("Name").performTextInput("Profile 1")
        composeTestRule.onNodeWithText("Activity Type").performClick()
        composeTestRule.onNodeWithText("Running").performClick()
        composeTestRule.onNodeWithText("Event Type").performClick()
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
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)
        dao.saveProfile(ProfileEntity(id = 5, name = "Profile 1", activityType = ActivityType.Running, eventType = EventType(id = 1, name = "Race")))

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_form") { composeTestRule.onNodeWithTag("quick_edit_form").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Profiles").performClick()
        composeTestRule.waitUntil(conditionDescription = "profiles_content") { composeTestRule.onNodeWithTag("profiles_content").isDisplayed() }
        composeTestRule.onNodeWithText("Profile 1").performClick()
        composeTestRule.waitUntil(conditionDescription = "profile_form") { composeTestRule.onNodeWithTag("profile_form").isDisplayed() }

        composeTestRule.onNodeWithText("Name").performTextClearance()
        composeTestRule.onNodeWithText("Name").performTextInput("Profile 2")
        composeTestRule.onNodeWithText("Activity Type").performClick()
        composeTestRule.onNodeWithText("Cycling").performClick()
        composeTestRule.onNodeWithText("Event Type").performClick()
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
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)
        dao.saveProfile(ProfileEntity(id = 10, name = "Profile 1", activityType = ActivityType.Running, eventType = EventType(id = 1, name = "Race")))

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_form") { composeTestRule.onNodeWithTag("quick_edit_form").isDisplayed() }
        composeTestRule.onNodeWithTag("menu").performClick()
        composeTestRule.onNodeWithText("Profiles").performClick()
        composeTestRule.waitUntil(conditionDescription = "profiles_content") { composeTestRule.onNodeWithTag("profiles_content").isDisplayed() }
        composeTestRule.onNodeWithTag("delete_profile_10").performClick()

        composeTestRule.onNodeWithText("Profile 1").isNotDisplayed()
    }

    @Test
    fun `Update activity`() = runTest {
        authStore.saveUser(user)
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)
        dao.saveProfile(ProfileEntity(name = "Profile 1", activityType = ActivityType.Cycling, eventType = EventType(id = 1, name = "Race")))

        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.waitUntil(conditionDescription = "quick_edit_form") { composeTestRule.onNodeWithTag("quick_edit_form").isDisplayed() }
        composeTestRule.onNodeWithText("Activity").performClick()
        composeTestRule.onNodeWithText("Activity 1").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Profile 1").performClick()
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.waitUntil(conditionDescription = "Activity updated") { composeTestRule.onNodeWithText("Activity updated").isDisplayed() }
    }
}