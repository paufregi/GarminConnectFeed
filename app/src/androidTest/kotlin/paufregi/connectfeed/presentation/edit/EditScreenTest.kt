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
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class EditScreenTest {

    private val activities = listOf(
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

    private val stravaActivities = listOf(
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

    private val eventTypes = listOf(
        EventType.Training,
        EventType.Transportation
    )

    private val courses = listOf(
        Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
        Course(id = 2, name = "course 2", distance = 15007.00, type = ActivityType.Cycling),
    )

    private val gears = listOf(
        Gear(id = "bike-1", name = "Bike 1", type = GearType.Bike),
        Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe),
    )

    private val incompatibleGears = listOf(
        Gear(id = "shoe-2", name = "Shoe 2", type = GearType.Shoe)
    )
    
    @Test
    fun `Base screen`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    eventTypes = eventTypes,
                    courses = courses
                )
            )
        }

        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Name").assertIsNotDisplayed()
        onNodeWithText("Event type").assertIsNotDisplayed()
        onNodeWithText("Course").assertIsNotDisplayed()
        onNodeWithText("Gear").assertIsNotDisplayed()
        onNodeWithText("Description").assertIsNotDisplayed()
        onNodeWithText("Water").assertIsNotDisplayed()
        onNodeWithTag("feel_text").assertIsNotDisplayed()
        onNodeWithTag("effort_text").assertIsNotDisplayed()
        onNodeWithTag("training_effect_checkbox").assertIsNotDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Activity selected`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    eventTypes = eventTypes,
                    courses = courses,
                    gears = gears,
                    activity = activities[0],
                    eventType = eventTypes[0],
                    course = courses[0],
                    gear = gears[1],
                    name = "New name",
                    water = 10,
                    feel = 50f,
                    effort = 80f,
                )
            )
        }

        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Name").assertTextContains("New name")
        onNodeWithText("Event type").assertTextContains(eventTypes[0].name)
        onNodeWithText("Course").assertTextContains(courses[0].name)
        onNodeWithText("Gear").assertTextContains(gears[1].name)
        onNodeWithText("Description").assertIsNotDisplayed()
        onNodeWithText("Water").assertTextContains("10")
        onNodeWithTag("feel_text").assertTextContains("Normal")
        onNodeWithTag("effort_text").assertTextContains("8 - Very Hard")
        onNodeWithTag("training_effect_checkbox").assertIsNotDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Activity selected - with Strava`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    stravaActivities = stravaActivities,
                    eventTypes = eventTypes,
                    courses = courses,
                    gears = gears,
                    activity = activities[0],
                    stravaActivity = stravaActivities[0],
                    eventType = eventTypes[0],
                    course = courses[0],
                    gear = gears[1],
                    description = "random text",
                    water = 10,
                    feel = 50f,
                    effort = 80f,
                    trainingEffect = true,
                )
            )
        }

        onNodeWithText("Description").assertTextContains("random text")
        onNodeWithTag("training_effect_checkbox").assertIsOn()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Activity selected - no gears available`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    eventTypes = eventTypes,
                    courses = courses,
                    gears = incompatibleGears,
                    activity = activities[1],
                )
            )
        }

        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Name").assertIsDisplayed()
        onNodeWithText("Event type").assertIsDisplayed()
        onNodeWithText("Course").assertIsDisplayed()
        onNodeWithText("Gear").assertIsNotDisplayed()
        onNodeWithText("Description").assertIsNotDisplayed()
    }

    @Test
    fun `Loading spinner`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Processing,
                )
            )
        }

        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Update success`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Success("Activity updated"),
                )
            )
        }

        onNodeWithTag("status_info_text").assertIsDisplayed()
        onNodeWithText("Activity updated").assertIsDisplayed()
    }

    @Test
    fun `Update failure`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Failure("Couldn't update activity"),
                )
            )
        }

        onNodeWithTag("status_info_text").assertIsDisplayed()
        onNodeWithText("Couldn't update activity").assertIsDisplayed()
    }
}