package paufregi.connectfeed.presentation.app.settings

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.presentation.ui.theme.Theme

@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    private val user = User(1, "Test User", "url")
    private val currentVersion = Version(1, 2, 3)

    @Test
    fun `Default settings`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                SettingsContent(
                    state = SettingsState(
                        user = user,
                        currentVersion = currentVersion,
                        latestRelease = Release(currentVersion, "url"),
                    )
                )
            }
        }

        onNodeWithText("Refresh").assertIsDisplayed()
        onNodeWithText("Connect").assertIsDisplayed()
        onNodeWithText("Sign out").assertIsDisplayed()
        onNodeWithText("Version: v1.2.3").assertIsDisplayed()
        onNodeWithText("Update").assertIsNotDisplayed()
    }

    @Test
    fun `Strava connection shows remove action`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                SettingsContent(
                    state = SettingsState(
                        user = user,
                        hasStrava = true,
                        currentVersion = currentVersion,
                        latestRelease = Release(currentVersion, "url"),
                    )
                )
            }
        }

        onNodeWithText("Remove").assertIsDisplayed()
        onNodeWithText("Connect").assertIsNotDisplayed()
    }

    @Test
    fun `Available update can be started`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                SettingsContent(
                    state = SettingsState(
                        user = user,
                        currentVersion = currentVersion,
                        latestRelease = Release(Version(2, 0, 0), "url"),
                    )
                )
            }
        }

        onNodeWithText("Update").assertIsDisplayed()
        onNodeWithText("Update").assertIsEnabled()
    }

    @Test
    fun `Updating disables update action and shows progress`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                SettingsContent(
                    state = SettingsState(
                        updating = true,
                        currentVersion = currentVersion,
                        latestRelease = Release(Version(2, 0, 0), "url"),
                    )
                )
            }
        }

        onNodeWithText("Update").assertIsNotEnabled()
        onNodeWithTag("updating_bar").assertIsDisplayed()
    }

    @Test
    fun `Loading state shows progress indicator`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                SettingsContent(state = SettingsState(loading = true))
            }
        }

        onNodeWithTag("loading").assertIsDisplayed()
        onNodeWithTag("settings_content").assertDoesNotExist()
    }
}
