package paufregi.connectfeed.presentation.syncweight

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class SyncWeightScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun `Status idle`() {
        composeTestRule.setContent {
            SyncWeightScreen(state = SyncWeightState(ProcessState.Idle))
        }
        composeTestRule.onNodeWithText("Don't know what to do").assertIsDisplayed()
    }

    @Test
    fun `Status uploading`() {
        composeTestRule.setContent {
            SyncWeightScreen(state = SyncWeightState(ProcessState.Processing))
        }
        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Status success`() {
        composeTestRule.setContent {
            SyncWeightScreen(state = SyncWeightState(ProcessState.Success("Sync succeeded")))
        }
        composeTestRule.onNodeWithText("Sync succeeded").assertIsDisplayed()
    }

    @Test
    fun `Status failure`() {
        composeTestRule.setContent {
            SyncWeightScreen(state = SyncWeightState(ProcessState.Failure("Sync failed")))
        }
        composeTestRule.onNodeWithText("Sync failed").assertIsDisplayed()
    }
}