package paufregi.connectfeed.presentation.app.activities.edit

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.presentation.ui.models.Status
import java.time.LocalDateTime

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class EditScreenTest {

    private val testActivity = Activity(
        id = 42,
        name = "Morning run",
        type = ActivityType.Running,
        eventType = EventType.Training,
        date = LocalDateTime.of(2025, 1, 2, 9, 30),
        stravaId = 101,
    )

    @Test
    fun `Loading status shows progress`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(state = EditState(activity = testActivity))
        }

        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Form displays editable activity fields`() = runAndroidComposeUiTest<ComponentActivity> {
        val course = Course(1, "City loop", 5000.0, ActivityType.Running)
        val gear = Gear("shoe-1", "Daily trainers", GearType.Shoe)
        setContent {
            EditContent(
                state = EditState(
                    status = null,
                    activity = testActivity,
                    eventTypes = listOf(EventType.Training),
                    courses = listOf(course),
                    gears = listOf(gear),
                    name = "Evening run",
                    eventType = EventType.Training,
                    course = course,
                    gear = gear,
                    description = "Easy pace",
                    water = 250,
                )
            )
        }

        onNodeWithTag("activity_42").assertIsDisplayed()
        onNodeWithText("Event type").assertIsDisplayed()
        onNodeWithText("Course").assertIsDisplayed()
        onNodeWithText("Gear").assertIsDisplayed()
        onNodeWithText("Description").assertIsDisplayed()
        onNodeWithText("Water").assertIsDisplayed()
        onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun `Failure status displays reason and resets status`() = runAndroidComposeUiTest<ComponentActivity> {
        var action: EditAction? = null
        setContent {
            EditContent(
                state = EditState(activity = testActivity, status = Status.Failure("Network error")),
                onAction = { action = it },
            )
        }

        onNodeWithText("Update failed!").assertIsDisplayed()
        onNodeWithText("Something went wrong: Network error").assertIsDisplayed()
        onNodeWithText("OK").performClick()

        assertEquals(EditAction.ResetStatus, action)
    }

    @Test
    fun `Success status displays completion`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            EditContent(state = EditState(activity = testActivity, status = Status.Success()))
        }

        onNodeWithText("Update completed!").assertIsDisplayed()
        onNodeWithTag("strava").assertIsDisplayed()
    }
}
