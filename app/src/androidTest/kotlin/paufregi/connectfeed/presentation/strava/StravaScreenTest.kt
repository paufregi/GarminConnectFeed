package paufregi.connectfeed.presentation.strava

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class StravaScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Loading spinning`() {
        composeTestRule.setContent {
            StravaScreen(state = StravaState(ProcessState.Processing))
        }
        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Strava linked`() {
        composeTestRule.setContent {
            StravaScreen(state = StravaState(ProcessState.Success("Strava linked")))
        }
        composeTestRule.onNodeWithText("Strava linked").assertIsDisplayed()
    }

    @Test
    fun `Sign in - failure`() {
        composeTestRule.setContent {
            StravaScreen(state = StravaState(ProcessState.Failure("Link failed")))
        }
        composeTestRule.onNodeWithText("Link failed").assertIsDisplayed()
    }
}