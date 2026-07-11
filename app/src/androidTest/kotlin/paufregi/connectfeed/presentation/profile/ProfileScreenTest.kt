package paufregi.connectfeed.presentation.profile

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @Test
    fun `Default values`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
            ))
        }
        onNodeWithText("Name").assertIsDisplayed()
        onNodeWithText("Type").assertIsDisplayed()
        onNodeWithText("Event type").assertIsDisplayed()
        onNodeWithText("Water").assertIsDisplayed()
        onNodeWithText("Rename activity").assertIsDisplayed()
        onNodeWithTag("rename_checkbox").assertIsOn()
        onNodeWithText("Customizable water").assertIsDisplayed()
        onNodeWithTag("custom_water_checkbox").assertIsOff()
        onNodeWithText("Feel & Effort").assertIsDisplayed()
        onNodeWithTag("feel_and_effort_checkbox").assertIsOff()
        onNodeWithText("Training effect").assertIsDisplayed()
        onNodeWithTag("training_effect_checkbox").assertIsOff()
        onNodeWithText("Cancel").assertIsDisplayed()
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Loading spinner`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Processing
            ))
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Edit profile`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "Profile 1",
                    type = ActivityType.Running,
                    eventType = EventType.Training,
                    course = Course(id = 1, name = "Course 1", distance = 10234.00, type = ActivityType.Running),
                    water = 10,
                    rename = true,
                    customWater = true,
                    feelAndEffort = true,
                    trainingEffect = true
                ))
            )
        }
        onNodeWithText("Name").assertTextContains("Profile 1")
        onNodeWithText("Type").assertTextContains("Running")
        onNodeWithText("Event type").assertTextContains(EventType.Training.name)
        onNodeWithText("Course").assertTextContains("Course 1")
        onNodeWithText("Water").assertTextContains("10")
        onNodeWithTag("rename_checkbox").assertIsOn()
        onNodeWithTag("custom_water_checkbox").assertIsOn()
        onNodeWithTag("feel_and_effort_checkbox").assertIsOn()
        onNodeWithTag("training_effect_checkbox").assertIsOn()
        onNodeWithText("Cancel").assertIsDisplayed()
        onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun `Invalid profile - no name`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "",
                    type = ActivityType.Running,
                    eventType = EventType.Training,
                    course = Course(id = 1, name = "Course 1", distance = 10234.00, type = ActivityType.Running),
                )
            ))
        }
        onNodeWithText("Name").assertTextContains("")
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Invalid profile - no event type`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "Profile 1",
                    type = ActivityType.Running,
                )
            ))
        }
        onNodeWithText("Event type").assertTextContains("")
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `No course - type any`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "Profile 1",
                    type = ActivityType.Any,
                    eventType = EventType.Training,
                )
            ))
        }
        onNodeWithText("Course").assertIsNotDisplayed()
        onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun `No course - type strength`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "Profile 1",
                    type = ActivityType.StrengthTraining,
                    eventType = EventType.Training
                )
            ))
        }
        onNodeWithText("Course").assertIsNotDisplayed()
        onNodeWithText("Save").assertIsDisplayed()
    }
}