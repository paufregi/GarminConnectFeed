package paufregi.connectfeed.presentation.profiles

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
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