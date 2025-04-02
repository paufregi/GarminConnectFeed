package paufregi.connectfeed.presentation.edit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.isDisplayed
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
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class EditScreenTest {

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

    val eventTypes = listOf(
        EventType.Training,
        EventType.Transportation
    )

    val courses = listOf(
        Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
        Course(id = 2, name = "course 2", distance = 15007.00, type = ActivityType.Cycling),
    )

    @Test
    fun `Default values`() {
        composeTestRule.setContent {
            EditContent(state = EditState(
                process = ProcessState.Idle,
                activities = activities,
                eventTypes = eventTypes,
                courses = courses
            ))
        }
        composeTestRule.onNodeWithText("Activity").isDisplayed()
        composeTestRule.onNodeWithText("Profile").isDisplayed()
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
            EditContent(state = EditState(
                process = ProcessState.Idle,
                activities = activities,
                stravaActivities = stravaActivities,
                eventTypes = eventTypes,
                courses = courses
            ))
        }
        composeTestRule.onNodeWithText("Activity").isDisplayed()
        composeTestRule.onNodeWithText("Strava").isDisplayed()
        composeTestRule.onNodeWithText("Profile").isDisplayed()
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
            EditContent(state = EditState(
                process = ProcessState.Idle,
                activities = activities,
                eventTypes = eventTypes,
                courses = courses,
                activity = activities[0],
                eventType = eventTypes[0],
                course = courses[0],
                water = 10,
                feel = 50f,
                effort = 80f,
            ))
        }
        composeTestRule.onNodeWithText("Activity").assertTextContains(activities[0].name)
        composeTestRule.onNodeWithText("Event type").assertTextContains(eventTypes[0].name)
        composeTestRule.onNodeWithText("Course").assertTextContains(courses[0].name)
        composeTestRule.onNodeWithText("Water").assertTextContains("10")
        composeTestRule.onNodeWithTag("feel_text").assertTextContains("Normal")
        composeTestRule.onNodeWithTag("effort_text").assertTextContains("8 - Very Hard")
        composeTestRule.onNodeWithTag("navigation_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Values selected - with Strava`() {
        composeTestRule.setContent {
            EditContent(state = EditState(
                process = ProcessState.Idle,
                activities = activities,
                stravaActivities = stravaActivities,
                eventTypes = eventTypes,
                courses = courses,
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                eventType = eventTypes[0],
                course = courses[0],
                description = "random text",
                water = 10,
                feel = 50f,
                effort = 80f,
                trainingEffect = true
            ))
        }
        composeTestRule.onNodeWithText("Activity").assertTextContains(activities[0].name)
        composeTestRule.onNodeWithText("Strava activity").assertTextContains(stravaActivities[0].name)
        composeTestRule.onNodeWithText("Event type").assertTextContains(eventTypes[0].name)
        composeTestRule.onNodeWithText("Course").assertTextContains(courses[0].name)
        composeTestRule.onNodeWithText("Description").assertTextContains("random text")
        composeTestRule.onNodeWithText("Water").assertTextContains("10")
        composeTestRule.onNodeWithTag("feel_text").assertTextContains("Normal")
        composeTestRule.onNodeWithTag("effort_text").assertTextContains("8 - Very Hard")
        composeTestRule.onNodeWithTag("training_effect_checkbox").assertIsOn()
        composeTestRule.onNodeWithTag("navigation_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Loading spinner`() {
        composeTestRule.setContent {
            EditContent(state = EditState(
                process = ProcessState.Processing,
            ))
        }
        composeTestRule.onNodeWithTag("loading").isDisplayed()
    }

    @Test
    fun `Update - success`() {
        composeTestRule.setContent {
            EditContent(state = EditState(
                process = ProcessState.Success("Activity updated"),
            ))
        }
        composeTestRule.onNodeWithTag("status_info_text").isDisplayed()
        composeTestRule.onNodeWithText("Activity updated").isDisplayed()
    }

    @Test
    fun `Update - failure`() {
        composeTestRule.setContent {
            EditContent(state = EditState(
                process = ProcessState.Failure("Couldn't update activity"),
            ))
        }
        composeTestRule.onNodeWithTag("status_info_text").isDisplayed()
        composeTestRule.onNodeWithText("Couldn't update activity").isDisplayed()
    }
}