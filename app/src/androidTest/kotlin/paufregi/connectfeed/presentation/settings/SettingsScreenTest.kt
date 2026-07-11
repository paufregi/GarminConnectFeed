package paufregi.connectfeed.presentation.settings

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    val user = User(1, "Paul", "url")
    val version = Version(1, 2, 3)
    
    @Test
    fun `Default screen`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SettingsContent(state = SettingsState(user = user, currentVersion = version, latestRelease = Release(version, "url")))
        }
        onNodeWithText("Refresh").assertIsDisplayed()
        onNodeWithText("Connect").assertIsDisplayed()
        onNodeWithText("Sign out").assertIsDisplayed()
        onNodeWithText("Version: v1.2.3").assertIsDisplayed()
        onNodeWithText("Update").assertIsNotDisplayed()
    }

    @Test
    fun `Default screen - Strava connected`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SettingsContent(state = SettingsState(user = user, hasStrava = true, currentVersion = version, latestRelease = Release(version, "url")))
        }
        onNodeWithText("Refresh").assertIsDisplayed()
        onNodeWithText("Remove").assertIsDisplayed()
        onNodeWithText("Sign out").assertIsDisplayed()
        onNodeWithText("Version: v1.2.3").assertIsDisplayed()
        onNodeWithText("Update").assertIsNotDisplayed()
    }

    @Test
    fun `Default screen - Update available`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SettingsContent(state = SettingsState(user = user, currentVersion = version, latestRelease = Release(Version(2,0,0), "url")))
        }
        onNodeWithText("Refresh").assertIsDisplayed()
        onNodeWithText("Connect").assertIsDisplayed()
        onNodeWithText("Sign out").assertIsDisplayed()
        onNodeWithText("Version: v1.2.3").assertIsDisplayed()
        onNodeWithText("Update").assertIsDisplayed()
    }

    @Test
    fun `Updating bar`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SettingsContent(state = SettingsState(updating = true, currentVersion = version, latestRelease = Release(Version(2,0,0), "url")))
        }
        onNodeWithText("Update").assertIsDisplayed()
        onNodeWithText("Update").assertIsNotEnabled()
        onNodeWithTag("updating_bar").assertIsDisplayed()
    }

    @Test
    fun `Loading spinner`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SettingsContent(state = SettingsState(process = ProcessState.Processing))
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Success process`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SettingsContent(state = SettingsState(process = ProcessState.Success("message")))
        }
        onNodeWithText("message").assertIsDisplayed()
        onNodeWithText("Ok").assertIsDisplayed()
    }

    @Test
    fun `Failed process`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SettingsContent(state = SettingsState(process = ProcessState.Failure("error")))
        }
        onNodeWithText("error").assertIsDisplayed()
        onNodeWithText("Ok").assertIsDisplayed()
    }
}