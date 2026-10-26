package paufregi.connectfeed.presentation.strava

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.presentation.ui.models.ProcessState

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class StravaScreenTest {

    @Test
    fun `Loading spinning`() = runComposeUiTest {
        setContent {
            StravaScreen(state = StravaState(ProcessState.Processing))
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Strava linked`() = runComposeUiTest {
        setContent {
            StravaScreen(state = StravaState(ProcessState.Success("Strava linked")))
        }
        onNodeWithText("Strava linked").assertIsDisplayed()
    }

    @Test
    fun `Sign in - failure`() = runComposeUiTest {
        setContent {
            StravaScreen(state = StravaState(ProcessState.Failure("Link failed")))
        }
        onNodeWithText("Link failed").assertIsDisplayed()
    }
}