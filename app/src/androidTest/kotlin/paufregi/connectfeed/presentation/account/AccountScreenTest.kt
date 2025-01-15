package paufregi.connectfeed.presentation.account

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class AccountScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Default screen`() {
        composeTestRule.setContent {
            AccountContent(state = AccountState(user = User("Paul", "url")))
        }
        composeTestRule.onNodeWithTag("profileImage").isDisplayed()
        composeTestRule.onNodeWithText("Refresh user").isDisplayed()
        composeTestRule.onNodeWithText("Sign out").isDisplayed()
    }

    @Test
    fun `Loading spinner`() {
        composeTestRule.setContent {
            AccountContent(state = AccountState(process = ProcessState.Processing))
        }
        composeTestRule.onNodeWithTag("loading").isDisplayed()
    }

    @Test
    fun `Success process`() {
        composeTestRule.setContent {
            AccountContent(state = AccountState(process = ProcessState.Success("message")))
        }
        composeTestRule.onNodeWithText("message").isDisplayed()
        composeTestRule.onNodeWithText("Ok").isDisplayed()
    }

    @Test
    fun `Failed process`() {
        composeTestRule.setContent {
            AccountContent(state = AccountState(process = ProcessState.Failure("error")))
        }
        composeTestRule.onNodeWithText("error").isDisplayed()
        composeTestRule.onNodeWithText("Ok").isDisplayed()
    }
}