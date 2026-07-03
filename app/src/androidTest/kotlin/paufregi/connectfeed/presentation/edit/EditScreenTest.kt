package paufregi.connectfeed.presentation.edit

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
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
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class EditScreenTest {

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
    fun `Default values`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(state = EditState(
                process = ProcessState.Idle,
                activities = activities,
                eventTypes = eventTypes,
                courses = courses
            ))
        }
        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Name").assertIsDisplayed()
        onNodeWithText("Event type").assertIsDisplayed()
        onNodeWithText("Course").assertIsNotDisplayed()
        onNodeWithText("Description").assertIsNotDisplayed()
        onNodeWithText("Water").assertIsDisplayed()
        onNodeWithTag("feel_text").assertTextContains("None selected")
        onNodeWithTag("effort_text").assertTextContains("0 - None selected")
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
            EditContent(state = EditState(
                process = ProcessState.Idle,
                activities = activities,
                stravaActivities = stravaActivities,
                eventTypes = eventTypes,
                courses = courses
            ))
        }
        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Name").assertIsDisplayed()
        onNodeWithText("Event type").assertIsDisplayed()
        onNodeWithText("Course").assertIsNotDisplayed()
        onNodeWithText("Description").assertIsNotDisplayed()
        onNodeWithText("Water").assertIsDisplayed()
        onNodeWithTag("feel_text").assertTextContains("None selected")
        onNodeWithTag("effort_text").assertTextContains("0 - None selected")
        onNodeWithTag("training_effect_checkbox").assertIsNotDisplayed()
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
            EditContent(state = EditState(
                process = ProcessState.Idle,
                activities = activities,
                name = "New name",
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
        onNodeWithText("Activity").assertTextContains(activities[0].name)
        onNodeWithText("Name").assertTextContains("New name")
        onNodeWithText("Event type").assertTextContains(eventTypes[0].name)
        onNodeWithText("Course").assertTextContains(courses[0].name)
        onNodeWithText("Description").assertIsNotDisplayed()
        onNodeWithText("Water").assertTextContains("10")
        onNodeWithTag("feel_text").assertTextContains("Normal")
        onNodeWithTag("effort_text").assertTextContains("8 - Very Hard")
        onNodeWithTag("navigation_bar").assertIsDisplayed()
        onNodeWithText("Edit").assertIsDisplayed()
        onNodeWithText("Quick Edit").assertIsDisplayed()
        onNodeWithText("Sync Strava").assertIsNotDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Values selected - with Strava`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(state = EditState(
                process = ProcessState.Idle,
                activities = activities,
                stravaActivities = stravaActivities,
                eventTypes = eventTypes,
                name = "New name",
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
        onNodeWithText("Activity").assertTextContains(activities[0].name)
        onNodeWithText("Strava activity").assertTextContains(stravaActivities[0].name)
        onNodeWithText("Name").assertTextContains("New name")
        onNodeWithText("Event type").assertTextContains(eventTypes[0].name)
        onNodeWithText("Course").assertTextContains(courses[0].name)
        onNodeWithText("Description").assertTextContains("random text")
        onNodeWithText("Water").assertTextContains("10")
        onNodeWithTag("feel_text").assertTextContains("Normal")
        onNodeWithTag("effort_text").assertTextContains("8 - Very Hard")
        onNodeWithTag("training_effect_checkbox").assertIsOn()
        onNodeWithTag("navigation_bar").assertIsDisplayed()
        onNodeWithText("Edit").assertIsDisplayed()
        onNodeWithText("Quick Edit").assertIsDisplayed()
        onNodeWithText("Sync Strava").assertIsDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Loading spinner` () = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(state = EditState(
                process = ProcessState.Processing,
            ))
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Update - success` () = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(state = EditState(
                process = ProcessState.Success("Activity updated"),
            ))
        }
        onNodeWithTag("status_info_text").assertIsDisplayed()
        onNodeWithText("Activity updated").assertIsDisplayed()
    }

    @Test
    fun `Update - failure` () = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(state = EditState(
                process = ProcessState.Failure("Couldn't update activity"),
            ))
        }
        onNodeWithTag("status_info_text").assertIsDisplayed()
        onNodeWithText("Couldn't update activity").assertIsDisplayed()
    }
}