package paufregi.connectfeed.presentation.app.profiles.edit

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @Test
    fun `Empty profile cannot be saved`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(loading = false))
        }

        onNodeWithText("Name").assertIsDisplayed()
        onNodeWithText("Type").assertIsDisplayed()
        onNodeWithText("Event type").assertIsDisplayed()
        onNodeWithText("Water").assertIsDisplayed()
        onNodeWithText("Rename activity").assertIsDisplayed()
        onNodeWithTag("rename_toggle").assertIsDisplayed()
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Existing profile values are displayed`() = runAndroidComposeUiTest<ComponentActivity> {
        val course = Course(
            id = 1,
            name = "City loop",
            distance = 10234.0,
            type = ActivityType.Running,
        )
        setContent {
            ProfileContent(
                state = ProfileState(
                    loading = false,
                    profile = Profile(
                        name = "Long run",
                        type = ActivityType.Running,
                        eventType = EventType.Training,
                        course = course,
                        water = 500,
                        rename = true,
                        customWater = true,
                        gear = true,
                        feelAndEffort = true,
                        trainingEffect = true,
                    ),
                    activityTypes = listOf(ActivityType.Any, ActivityType.Running),
                    eventTypes = listOf(EventType.Training),
                    courses = listOf(course),
                )
            )
        }

        onNodeWithText("Long run").assertIsDisplayed()
        onNodeWithText("Training").assertIsDisplayed()
        onNodeWithText("City loop").assertIsDisplayed()
        onNodeWithText("500").assertIsDisplayed()
        onNodeWithTag("rename_toggle").assertIsDisplayed()
        onNodeWithTag("custom_water_toggle").assertIsDisplayed()
        onNodeWithTag("gear_toggle").assertIsDisplayed()
        onNodeWithTag("feel_and_effort_toggle").assertIsDisplayed()
        onNodeWithTag("training_effect_toggle").assertIsDisplayed()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Running profile requires event type`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(
                state = ProfileState(
                    loading = false,
                    profile = Profile(name = "Long run", type = ActivityType.Running),
                    activityTypes = listOf(ActivityType.Running),
                )
            )
        }

        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Types without courses hide course field`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(
                state = ProfileState(
                    loading = false,
                    profile = Profile(
                        name = "General",
                        type = ActivityType.Any,
                        eventType = EventType.Training,
                    ),
                )
            )
        }

        onNodeWithText("Course").assertIsNotDisplayed()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Loading state shows form progress`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(loading = true))
        }

        onNodeWithTag("loading").assertIsDisplayed()
    }
}
