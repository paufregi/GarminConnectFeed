package paufregi.connectfeed.presentation.info

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
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class InfoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val version = Version(1, 2, 3)

    @Test
    fun `Default screen`() {
        composeTestRule.setContent {
            InfoContent(state = InfoState())
        }

        composeTestRule.onNodeWithTag("app_icon").isDisplayed()
        composeTestRule.onNodeWithText("Version: Unknown").isDisplayed()
        composeTestRule.onNodeWithText("Check for updates").isDisplayed()
    }

    @Test
    fun `Default screen - with version`() {
        composeTestRule.setContent {
            InfoContent(state = InfoState(currentVersion = version))
        }

        composeTestRule.onNodeWithTag("app_icon").isDisplayed()
        composeTestRule.onNodeWithText("Version: v1.2.3").isDisplayed()
        composeTestRule.onNodeWithText("Check for updates").isDisplayed()
    }

    @Test
    fun `Default screen - loading`() {
        composeTestRule.setContent {
            InfoContent(state = InfoState(currentVersion = version, process = ProcessState.Processing))
        }

        composeTestRule.onNodeWithTag("app_icon").isDisplayed()
        composeTestRule.onNodeWithText("Version: v1.2.3").isDisplayed()
        composeTestRule.onNodeWithText("Check for updates").isDisplayed()
        composeTestRule.onNodeWithText("Check for updates").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("loading").isDisplayed()
    }

    @Test
    fun `Default screen - failed`() {
        composeTestRule.setContent {
            InfoContent(state = InfoState(currentVersion = version, process = ProcessState.Failure("error")))
        }

        composeTestRule.onNodeWithTag("app_icon").isDisplayed()
        composeTestRule.onNodeWithText("Version: v1.2.3").isDisplayed()
        composeTestRule.onNodeWithText("error").isDisplayed()
        composeTestRule.onNodeWithText("Check for updates").isDisplayed()
    }

    @Test
    fun `Default screen - has update`() {
        composeTestRule.setContent {
            InfoContent(state = InfoState(
                currentVersion = version,
                latestRelease = Release(Version(1, 2, 4), "url"),
            ))
        }

        composeTestRule.onNodeWithTag("app_icon").isDisplayed()
        composeTestRule.onNodeWithText("Version: v1.2.3").isDisplayed()
        composeTestRule.onNodeWithText("Update available: v1.2.4").isDisplayed()
        composeTestRule.onNodeWithText("Check for updates").isDisplayed()
        composeTestRule.onNodeWithText("Update").isDisplayed()
    }

    @Test
    fun `Default screen - no update`() {
        composeTestRule.setContent {
            InfoContent(state = InfoState(
                currentVersion = version,
                latestRelease = Release(Version(1, 2, 3), "url"),
            ))
        }

        composeTestRule.onNodeWithTag("app_icon").isDisplayed()
        composeTestRule.onNodeWithText("Version: v1.2.3").isDisplayed()
        composeTestRule.onNodeWithText("Update available: v1.2.3").isNotDisplayed()
        composeTestRule.onNodeWithText("Check for updates").isDisplayed()
        composeTestRule.onNodeWithText("Update").isNotDisplayed()
    }
}