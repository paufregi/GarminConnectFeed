package paufregi.connectfeed.presentation.password

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class PasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Form - empty`() {
        composeTestRule.setContent {
            PasswordContent(state = PasswordState())
        }
        composeTestRule.onNodeWithText("Username").assertTextContains("")
        composeTestRule.onNodeWithText("Password").assertTextContains("")
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Form - filled`() {
        composeTestRule.setContent {
            PasswordContent(state = PasswordState(credential = Credential("userTest", "passTest")))
        }
        composeTestRule.onNodeWithText("Username").assertTextContains("userTest")
        composeTestRule.onNodeWithText("Password").assertTextContains("••••••••")
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Form - filled with visible password`() {
        composeTestRule.setContent {
            PasswordContent(state = PasswordState(credential = Credential("userTest", "passTest"), showPassword = true))
        }
        composeTestRule.onNodeWithText("Username").assertTextContains("userTest")
        composeTestRule.onNodeWithText("Password").assertTextContains("passTest")
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Loading spinning`() {
        composeTestRule.setContent {
            PasswordContent(state = PasswordState(process = ProcessState.Processing))
        }
        composeTestRule.onNodeWithTag("loading").isDisplayed()
    }

    @Test
    fun `Sign in - success`() {
        composeTestRule.setContent {
            PasswordContent(state = PasswordState(process = ProcessState.Success("Paul")))
        }
        composeTestRule.onNodeWithText("Password changed").isDisplayed()
        composeTestRule.onNodeWithText("Ok").isDisplayed()
    }

    @Test
    fun `Sign in - failure`() {
        composeTestRule.setContent {
            PasswordContent(state = PasswordState(process = ProcessState.Failure("error")))
        }
        composeTestRule.onNodeWithText("error").isDisplayed()
        composeTestRule.onNodeWithText("Ok").isDisplayed()
    }
}