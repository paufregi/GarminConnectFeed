package paufregi.connectfeed.presentation.app.activities.list

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
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.User
import java.time.LocalDateTime

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class ActivitiesScreenTest {

    private val testActivity = Activity(
        id = 42,
        name = "Morning run",
        type = ActivityType.Running,
        eventType = EventType.Training,
        distance = 5000.0,
        date = LocalDateTime.of(2025, 1, 2, 9, 30),
        stravaId = 101,
    )

    @Test
    fun `Empty list`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ActivitiesContent(
                state = ActivitiesState(loading = false),
                user = User.ANONYM,
            )
        }

        onNodeWithText("No activities").assertIsDisplayed()
    }

    @Test
    fun `Activity list displays activities`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ActivitiesContent(
                state = ActivitiesState(loading = false, activities = listOf(testActivity)),
                user = User.ANONYM,
            )
        }

        onNodeWithText("Morning run").assertIsDisplayed()
        onNodeWithText("5.00 km").assertIsDisplayed()
        onNodeWithTag("activity_42").assertIsDisplayed()
    }

    @Test
    fun `Selecting activity returns Garmin and Strava ids`() = runAndroidComposeUiTest<ComponentActivity> {
        var selected: Pair<Long, Long?>? = null
        setContent {
            ActivitiesContent(
                state = ActivitiesState(loading = false, activities = listOf(testActivity)),
                user = User.ANONYM,
                onOpen = { id, stravaId -> selected = id to stravaId },
            )
        }

        onNodeWithTag("activity_42").performClick()

        assertEquals(42L to 101L, selected)
    }

    @Test
    fun `Loading state hides activity list`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ActivitiesContent(
                state = ActivitiesState(loading = true),
                user = User.ANONYM,
            )
        }

        onNodeWithTag("loading").assertIsDisplayed()
    }
}
