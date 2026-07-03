package paufregi.connectfeed.presentation.profiles

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class ProfilesScreenTest {

    @Test
    fun `Default values`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfilesContent(state = ProfilesState())
        }
        onNodeWithText("No profile").assertIsDisplayed()
    }

    @Test
    fun `Profile list`() = runAndroidComposeUiTest<ComponentActivity> {
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