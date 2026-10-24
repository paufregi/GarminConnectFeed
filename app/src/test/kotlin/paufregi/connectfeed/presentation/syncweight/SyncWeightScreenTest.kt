package paufregi.connectfeed.presentation.syncweight

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.presentation.ui.models.ProcState

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class SyncWeightScreenTest {

    @Test
    fun `Running status shows loading screen`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SyncWeightContent(state = ProcState.Running)
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Success status shows message`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SyncWeightContent(state = ProcState.Success("Sync succeeded"))
        }
        onNodeWithText("Sync succeeded").assertIsDisplayed()
    }

    @Test
    fun `Failure status shows message and reason`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SyncWeightContent(state = ProcState.Failure("Invalid file"))
        }
        onNodeWithText("Sync failed!").assertIsDisplayed()
        onNodeWithText("Something went wrong: Invalid file").assertIsDisplayed()
    }
}