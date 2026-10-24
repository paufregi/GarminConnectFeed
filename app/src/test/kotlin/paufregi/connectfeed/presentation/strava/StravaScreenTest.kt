package paufregi.connectfeed.presentation.strava

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
import paufregi.connectfeed.presentation.ui.models.Status

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class StravaScreenTest {

    @Test
    fun `Running status shows loading screen`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            StravaContent(state = Status.Loading)
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Success status shows linked message`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            StravaContent(state = Status.Success())
        }
        onNodeWithText("Strava linked!").assertIsDisplayed()
    }

    @Test
    fun `Failure status shows message and reason`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            StravaContent(state = Status.Failure("Authorization failed"))
        }
        onNodeWithText("Link failed!").assertIsDisplayed()
        onNodeWithText("Something went wrong: Authorization failed").assertIsDisplayed()
    }
}