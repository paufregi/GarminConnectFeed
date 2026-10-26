package paufregi.connectfeed.presentation.profiles

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class ProfilesScreenTest {

    @Test
    fun `Default values`() = runComposeUiTest {
        setContent {
            ProfilesContent(state = ProfilesState())
        }
        onNodeWithText("No profile").assertIsDisplayed()
    }

    @Test
    fun `Profile list`() = runComposeUiTest {
        setContent {
            ProfilesContent(state = ProfilesState(
                profiles = listOf(
                    Profile(name = "Profile 1", type = ActivityType.Running),
                    Profile(name = "Profile 2", type = ActivityType.Cycling),
                    Profile(name = "Profile 3", type = ActivityType.Running)
                )
            ))
        }
        onNodeWithText("Profile 1").assertIsDisplayed()
        onNodeWithText("Profile 2").assertIsDisplayed()
        onNodeWithText("Profile 3").assertIsDisplayed()
    }

}