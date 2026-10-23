package paufregi.connectfeed.presentation.app.activities.quickedit

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
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
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Profile
import java.time.LocalDateTime

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class QuickEditScreenTest {

    private val testActivity = Activity(
        id = 42,
        name = "Morning run",
        type = ActivityType.Running,
        eventType = EventType.Training,
        date = LocalDateTime.of(2025, 1, 2, 9, 30),
        stravaId = 101,
    )

    @Test
    fun `No profile shows empty list and disables save`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    activity = testActivity,
                    profiles = emptyList(),
                )
            )
        }

        onNodeWithText("No profiles").assertIsDisplayed()
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Selecting compatible profile emits profile action`() = runAndroidComposeUiTest<ComponentActivity> {
        val profile = Profile(id = 7, name = "Long run", type = ActivityType.Running)
        var action: QuickEditAction? = null
        setContent {
            QuickEditContent(
                state = QuickEditState(activity = testActivity, profiles = listOf(profile)),
                onAction = { action = it },
            )
        }

        onNodeWithTag("profile_7").performClick()

        assertEquals(QuickEditAction.SetProfile(profile), action)
    }

    @Test
    fun `Selected profile displays applicable edit fields`() = runAndroidComposeUiTest<ComponentActivity> {
        val gear = Gear("shoe-1", "Daily trainers", GearType.Shoe)
        val profile = Profile(
            id = 7,
            name = "Long run",
            type = ActivityType.Running,
            gear = true,
            customWater = true,
            feelAndEffort = true,
        )
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    activity = testActivity,
                    profiles = listOf(profile),
                    gears = listOf(gear),
                    profile = profile,
                    gear = gear,
                    description = "Easy pace",
                    water = 250,
                )
            )
        }

        onNodeWithTag("activity_42").assertIsDisplayed()
        onNodeWithTag("profile_7").assertIsDisplayed()
        onNodeWithText("Gear").assertIsDisplayed()
        onNodeWithText("Description").assertIsDisplayed()
        onNodeWithText("Water").assertIsDisplayed()
        onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun `Failure status displays reason and resets status`() = runAndroidComposeUiTest<ComponentActivity> {
        var action: QuickEditAction? = null
        setContent {
            QuickEditContent(
                state = QuickEditState(activity = testActivity, status = Status.Failure("Update rejected")),
                onAction = { action = it },
            )
        }

        onNodeWithText("Update failed!").assertIsDisplayed()
        onNodeWithText("Something went wrong: Update rejected").assertIsDisplayed()
        onNodeWithText("OK").performClick()

        assertEquals(QuickEditAction.ResetStatus, action)
    }

    @Test
    fun `Success status displays completion`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(activity = testActivity, status = Status.Success))
        }

        onNodeWithText("Update completed!").assertIsDisplayed()
        onNodeWithTag("strava").assertIsDisplayed()
    }

    @Test
    fun `Loading status shows progress`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            QuickEditContent(state = QuickEditState(activity = testActivity, status = Status.Loading))
        }

        onNodeWithTag("loading").assertIsDisplayed()
    }
}
