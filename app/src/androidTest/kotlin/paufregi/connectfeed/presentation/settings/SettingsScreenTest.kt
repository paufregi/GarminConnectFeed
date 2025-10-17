package paufregi.connectfeed.presentation.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    val user = User(1, "Paul", "url")
    val version = Version(1, 2, 3)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Default screen`() {
        composeTestRule.setContent {
            SettingsContent(state = SettingsState(user = user, currentVersion = version, latestRelease = Release(version, "url")))
        }
        composeTestRule.onNodeWithTag("profileImage").isDisplayed()
        composeTestRule.onNodeWithText("Refresh").isDisplayed()
        composeTestRule.onNodeWithText("Connect").isDisplayed()
        composeTestRule.onNodeWithText("Sign out").isDisplayed()
        composeTestRule.onNodeWithText("Version: v1.2.3").isDisplayed()
        composeTestRule.onNodeWithText("Update").isNotDisplayed()
    }

    @Test
    fun `Default screen - Strava connected`() {
        composeTestRule.setContent {
            SettingsContent(state = SettingsState(user = user, hasStrava = true, currentVersion = version, latestRelease = Release(version, "url")))
        }
        composeTestRule.onNodeWithTag("profileImage").isDisplayed()
        composeTestRule.onNodeWithText("Refresh").isDisplayed()
        composeTestRule.onNodeWithText("Remove").isDisplayed()
        composeTestRule.onNodeWithText("Sign out").isDisplayed()
        composeTestRule.onNodeWithText("Version: v1.2.3").isDisplayed()
        composeTestRule.onNodeWithText("Update").isNotDisplayed()
    }

    @Test
    fun `Default screen - Update available`() {
        composeTestRule.setContent {
            SettingsContent(state = SettingsState(user = user, currentVersion = version, latestRelease = Release(Version(2,0,0), "url")))
        }
        composeTestRule.onNodeWithTag("profileImage").isDisplayed()
        composeTestRule.onNodeWithText("Refresh").isDisplayed()
        composeTestRule.onNodeWithText("Remove").isDisplayed()
        composeTestRule.onNodeWithText("Sign out").isDisplayed()
        composeTestRule.onNodeWithText("Version: v1.2.3").isDisplayed()
        composeTestRule.onNodeWithText("Update").isDisplayed()
    }

    @Test
    fun `Updating bar`() {
        composeTestRule.setContent {
            SettingsContent(state = SettingsState(updating = true, currentVersion = version, latestRelease = Release(Version(2,0,0), "url")))
        }
        composeTestRule.onNodeWithText("Update").isDisplayed()
        composeTestRule.onNodeWithText("Update").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("updating_bar").isDisplayed()
    }

    @Test
    fun `Loading spinner`() {
        composeTestRule.setContent {
            SettingsContent(state = SettingsState(process = ProcessState.Processing))
        }
        composeTestRule.onNodeWithTag("loading").isDisplayed()
    }

    @Test
    fun `Success process`() {
        composeTestRule.setContent {
            SettingsContent(state = SettingsState(process = ProcessState.Success("message")))
        }
        composeTestRule.onNodeWithText("message").isDisplayed()
        composeTestRule.onNodeWithText("Ok").isDisplayed()
    }

    @Test
    fun `Failed process`() {
        composeTestRule.setContent {
            SettingsContent(state = SettingsState(process = ProcessState.Failure("error")))
        }
        composeTestRule.onNodeWithText("error").isDisplayed()
        composeTestRule.onNodeWithText("Ok").isDisplayed()
    }
}