package paufregi.connectfeed.presentation.syncweight

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class SyncWeightScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @Test
    fun `Status idle`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SyncWeightScreen(state = SyncWeightState(ProcessState.Idle))
        }
        onNodeWithText("Don't know what to do").assertIsDisplayed()
    }

    @Test
    fun `Status uploading`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SyncWeightScreen(state = SyncWeightState(ProcessState.Processing))
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Status success`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SyncWeightScreen(state = SyncWeightState(ProcessState.Success("Sync succeeded")))
        }
        onNodeWithText("Sync succeeded").assertIsDisplayed()
    }

    @Test
    fun `Status failure`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SyncWeightScreen(state = SyncWeightState(ProcessState.Failure("Sync failed")))
        }
        onNodeWithText("Sync failed").assertIsDisplayed()
    }
}