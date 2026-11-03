package paufregi.connectfeed.presentation.edit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.presentation.ui.models.ProcessState

@ExperimentalTestApi
@ExperimentalMaterial3Api
@RunWith(RobolectricTestRunner::class)
class EditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
    }
    
    @Test
    fun `Base screen`() {
        composeTestRule.setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    eventTypes = eventTypes,
                    courses = courses
                )
            )
        }

        composeTestRule.onNodeWithText("Activity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Name").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Event type").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Course").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Gear").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Water").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("feel_text").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("effort_text").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("training_effect_checkbox").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Activity selected`() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("Activity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Name").assertTextContains("New name")
        composeTestRule.onNodeWithText("Event type").assertTextContains(eventTypes[0].name)
        composeTestRule.onNodeWithText("Course").assertTextContains(courses[0].name)
        composeTestRule.onNodeWithText("Gear").assertTextContains(gears[1].name)
        composeTestRule.onNodeWithText("Description").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Water").assertTextContains("10")
        composeTestRule.onNodeWithTag("feel_text").assertTextContains("Normal")
        composeTestRule.onNodeWithTag("effort_text").assertTextContains("8 - Very Hard")
        composeTestRule.onNodeWithTag("training_effect_checkbox").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Activity selected - with Strava`() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("Description").assertTextContains("random text")
        composeTestRule.onNodeWithTag("training_effect_checkbox").assertIsOn()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Activity selected - no gears available`() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("Activity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Event type").assertIsDisplayed()
        composeTestRule.onNodeWithText("Course").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gear").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsNotDisplayed()
    }

    @Test
    fun `Loading spinner`() {
        composeTestRule.setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Processing,
                )
            )
        }

        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Update success`() {
        composeTestRule.setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Success("Activity updated"),
                )
            )
        }

        composeTestRule.onNodeWithTag("status_info_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Activity updated").assertIsDisplayed()
    }

    @Test
    fun `Update failure`() {
        composeTestRule.setContent {
            EditContent(
                state = EditState(
                    process = ProcessState.Failure("Couldn't update activity"),
                )
            )
        }

        composeTestRule.onNodeWithTag("status_info_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Couldn't update activity").assertIsDisplayed()
    }
}