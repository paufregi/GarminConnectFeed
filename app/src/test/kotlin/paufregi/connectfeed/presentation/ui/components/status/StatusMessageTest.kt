package paufregi.connectfeed.presentation.ui.components.status

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Activity

@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class StatusMessageTest {

    @Test
    fun `Success message shows message and extra content`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            SuccessMessage(message = "All good") { Text("Extra") }
        }

        onNodeWithTag("icon").assertIsDisplayed()
        onNodeWithTag("message").assertTextEquals("All good")
        onNodeWithTag("reason").assertDoesNotExist()
        onNodeWithText("Extra").assertIsDisplayed()
    }

    @Test
    fun `Failure message shows message and reason`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            FailureMessage(message = "Oops", reason = "network down")
        }

        onNodeWithTag("icon").assertIsDisplayed()
        onNodeWithTag("message").assertTextEquals("Oops")
        onNodeWithTag("reason").assertTextEquals("Something went wrong: network down")
    }

    @Test
    fun `Activity links show Garmin only without Strava id`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ActivityLinks(Activity.EMPTY.copy(id = 1))
        }

        onNodeWithTag("garmin").assertIsDisplayed()
        onNodeWithTag("strava").assertDoesNotExist()
    }

    @Test
    fun `Activity links show Garmin and Strava with Strava id`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ActivityLinks(Activity.EMPTY.copy(id = 1, stravaId = 2))
        }

        onNodeWithTag("garmin").assertIsDisplayed()
        onNodeWithTag("strava").assertIsDisplayed()
    }
}

