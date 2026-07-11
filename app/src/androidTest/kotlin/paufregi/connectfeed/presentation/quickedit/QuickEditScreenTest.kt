package paufregi.connectfeed.presentation.quickedit

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class QuickEditScreenTest {

    val activities = listOf(
        Activity(
            id = 1L,
            name = "Running",
            type = ActivityType.Running,
            distance = 10234.00,
            trainingEffect = "recovery"
        ),
        Activity(
            id  = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            distance = 17803.00,
            trainingEffect = "recovery"
        )
    )

    val stravaActivities = listOf(
        Activity(
            id = 1L,
            name = "Running",
            type = ActivityType.Running,
            distance = 10234.00
        ),
        Activity(
            id = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            distance = 17803.00
        )
    )

    val profiles = listOf(
        Profile(name = "profile1", type = ActivityType.Running),
        Profile(name = "profile2", type = ActivityType.Cycling),
        Profile(name = "profile3", type = ActivityType.Running)
    )

    @Test
    fun `Default values`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Idle,
                activities = activities,
                profiles = profiles,
            ))
        }
        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
        onNodeWithTag("navigation_bar").assertIsDisplayed()
        onNodeWithText("Edit").assertIsDisplayed()
        onNodeWithText("Quick Edit").assertIsDisplayed()
        onNodeWithText("Strava Sync").assertIsNotDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Default values - with Strava`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Idle,
                activities = activities,
                stravaActivities = stravaActivities,
                profiles = profiles,
            ))
        }
        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Strava activity").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
        onNodeWithTag("navigation_bar").assertIsDisplayed()
        onNodeWithText("Edit").assertIsDisplayed()
        onNodeWithText("Quick Edit").assertIsDisplayed()
        onNodeWithText("Sync Strava").assertIsDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Values selected`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Idle,
                activities = activities,
                profiles = profiles,
                activity = activities[0],
                profile = profiles[0],
            ))
        }
        onNodeWithText("Activity").assertTextContains(activities[0].name)
        onNodeWithText("Profile").assertTextContains(profiles[0].name)
        onNodeWithTag("navigation_bar").assertIsDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Values selected - with Strava`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Idle,
                activities = activities,
                stravaActivities = stravaActivities,
                profiles = profiles,
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = profiles[0],
            ))
        }
        onNodeWithText("Activity").assertTextContains(activities[0].name)
        onNodeWithText("Strava activity").assertTextContains(stravaActivities[0].name)
        onNodeWithText("Profile").assertTextContains(profiles[0].name)
        onNodeWithTag("navigation_bar").assertIsDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Loading spinner`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Processing,
            ))
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Update - success`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Success("Activity updated"),
            ))
        }
        onNodeWithTag("status_info_text").assertIsDisplayed()
        onNodeWithText("Activity updated").assertIsDisplayed()
    }

    @Test
    fun `Update - failure`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Failure("Couldn't update activity"),
            ))
        }
        onNodeWithTag("status_info_text").assertIsDisplayed()
        onNodeWithText("Couldn't update activity").assertIsDisplayed()
    }
}