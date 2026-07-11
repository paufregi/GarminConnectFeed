package paufregi.connectfeed.presentation.strava

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class StravaScreenTest {

    @Test
    fun `Loading spinning`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            StravaScreen(state = StravaState(ProcessState.Processing))
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Strava linked`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            StravaScreen(state = StravaState(ProcessState.Success("Strava linked")))
        }
        onNodeWithText("Strava linked").assertIsDisplayed()
    }

    @Test
    fun `Sign in - failure`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            StravaScreen(state = StravaState(ProcessState.Failure("Link failed")))
        }
        onNodeWithText("Link failed").assertIsDisplayed()
    }
}