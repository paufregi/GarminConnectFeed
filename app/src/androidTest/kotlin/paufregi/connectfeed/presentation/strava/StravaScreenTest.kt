package paufregi.connectfeed.presentation.strava

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class StravaScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Loading spinning`() {
        composeTestRule.setContent {
            StravaScreen(state = StravaState.Processing)
        }
        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Strava linked`() {
        composeTestRule.setContent {
            StravaScreen(state = StravaState.Success)
        }
        composeTestRule.onNodeWithText("Strava linked").isDisplayed()
    }

    @Test
    fun `Sign in - failure`() {
        composeTestRule.setContent {
            StravaScreen(state = StravaState.Failure)
        }
        composeTestRule.onNodeWithText("Link failed").isDisplayed()
    }
}