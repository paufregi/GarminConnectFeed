package paufregi.connectfeed.presentation.profiles

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile

@ExperimentalTestApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner  ::class)
class ProfilesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Default values`() {
        composeTestRule.setContent {
            ProfilesContent(state = ProfilesState())
        }
        composeTestRule.onNodeWithText("No profile").assertIsDisplayed()
    }

    @Test
    fun `Profile list`() {
        composeTestRule.setContent {
            ProfilesContent(state = ProfilesState(
                profiles = listOf(
                    Profile(name = "Profile 1", type = ActivityType.Running),
                    Profile(name = "Profile 2", type = ActivityType.Cycling),
                    Profile(name = "Profile 3", type = ActivityType.Running)
                )
            ))
        }
        composeTestRule.onNodeWithText("Profile 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile 3").assertIsDisplayed()
    }

}