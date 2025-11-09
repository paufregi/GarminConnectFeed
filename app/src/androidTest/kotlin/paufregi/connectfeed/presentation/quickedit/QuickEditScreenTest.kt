package paufregi.connectfeed.presentation.quickedit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile
 import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class QuickEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
    fun `Default values`() {
        composeTestRule.setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Idle,
                activities = activities,
                profiles = profiles,
            ))
        }
        composeTestRule.onNodeWithText("Activity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
        composeTestRule.onNodeWithTag("navigation_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Edit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quick Edit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Strava Sync").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Default values - with Strava`() {
        composeTestRule.setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Idle,
                activities = activities,
                stravaActivities = stravaActivities,
                profiles = profiles,
            ))
        }
        composeTestRule.onNodeWithText("Activity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Strava activity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
        composeTestRule.onNodeWithTag("navigation_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Edit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quick Edit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sync Strava").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Values selected`() {
        composeTestRule.setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Idle,
                activities = activities,
                profiles = profiles,
                activity = activities[0],
                profile = profiles[0],
            ))
        }
        composeTestRule.onNodeWithText("Activity").assertTextContains(activities[0].name)
        composeTestRule.onNodeWithText("Profile").assertTextContains(profiles[0].name)
        composeTestRule.onNodeWithTag("navigation_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Values selected - with Strava`() {
        composeTestRule.setContent {
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
        composeTestRule.onNodeWithText("Activity").assertTextContains(activities[0].name)
        composeTestRule.onNodeWithText("Strava activity").assertTextContains(stravaActivities[0].name)
        composeTestRule.onNodeWithText("Profile").assertTextContains(profiles[0].name)
        composeTestRule.onNodeWithTag("navigation_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Loading spinner`() {
        composeTestRule.setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Processing,
            ))
        }
        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Update - success`() {
        composeTestRule.setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Success("Activity updated"),
            ))
        }
        composeTestRule.onNodeWithTag("status_info_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Activity updated").assertIsDisplayed()
    }

    @Test
    fun `Update - failure`() {
        composeTestRule.setContent {
            QuickEditContent(state = QuickEditState(
                process = ProcessState.Failure("Couldn't update activity"),
            ))
        }
        composeTestRule.onNodeWithTag("status_info_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Couldn't update activity").assertIsDisplayed()
    }
}