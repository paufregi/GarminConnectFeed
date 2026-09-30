package paufregi.connectfeed.presentation.profiles

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.app.profiles.ProfilesContent
import paufregi.connectfeed.presentation.app.profiles.list.ProfileForm
import paufregi.connectfeed.presentation.app.profiles.list.ProfileFormAction
import paufregi.connectfeed.presentation.app.profiles.list.ProfilesAction
import paufregi.connectfeed.presentation.app.profiles.list.ProfilesMode
import paufregi.connectfeed.presentation.app.profiles.list.ProfilesState

@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class ProfilesScreenTest {
    @Test
    fun `Profile click opens form and cancel returns list`() = runAndroidComposeUiTest<ComponentActivity> {
        val profile = Profile(
            id = 1,
            name = "Long run",
            type = ActivityType.Running,
            eventType = EventType.Training,
            gear = true,
        )

        setContent {
            var state by remember { mutableStateOf(ProfilesState(profiles = listOf(profile))) }
            ProfilesContent(
                state = state,
                onListAction = { action ->
                    when (action) {
                        is ProfilesAction.Edit -> state = state.copy(mode = ProfilesMode.Edit, profile = action.profile)
                        else -> Unit
                    }
                },
                onFormAction = { action ->
                    when (action) {
                        ProfileFormAction.Cancel -> state = state.copy(mode = ProfilesMode.List, profile = Profile())
                        else -> Unit
                    }
                }
            )
        }

        onNodeWithTag("profile_1").performClick()
        onNodeWithTag("profile_form_content").assertIsDisplayed()
        onNodeWithTag("profile_name").assertIsDisplayed()
        onNodeWithTag("cancel_profile").performClick()
        onNodeWithTag("profile_list_content").assertIsDisplayed()
    }

    @Test
    fun `Save button disabled when profile is invalid`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileForm(
                state = ProfilesState(
                    mode = ProfilesMode.Edit,
                    profile = Profile(name = "", type = ActivityType.Running, eventType = null),
                    activityTypes = listOf(ActivityType.Any, ActivityType.Running),
                    eventTypes = listOf(EventType.Training),
                )
            )
        }

        onNodeWithTag("save_profile").assertIsNotEnabled()
    }
}


