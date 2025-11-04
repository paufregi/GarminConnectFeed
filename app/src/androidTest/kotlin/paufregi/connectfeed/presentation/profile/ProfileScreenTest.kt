package paufregi.connectfeed.presentation.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.ProfileType
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Default values`() {
        composeTestRule.setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
            ))
        }
        composeTestRule.onNodeWithText("Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type").assertIsDisplayed()
        composeTestRule.onNodeWithText("Event type").assertIsDisplayed()
        composeTestRule.onNodeWithText("Water").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rename activity").assertIsDisplayed()
        composeTestRule.onNodeWithTag("rename_checkbox").assertIsOn()
        composeTestRule.onNodeWithText("Customizable water").assertIsDisplayed()
        composeTestRule.onNodeWithTag("custom_water_checkbox").assertIsOff()
        composeTestRule.onNodeWithText("Feel & Effort").assertIsDisplayed()
        composeTestRule.onNodeWithTag("feel_and_effort_checkbox").assertIsOff()
        composeTestRule.onNodeWithText("Training effect").assertIsDisplayed()
        composeTestRule.onNodeWithTag("training_effect_checkbox").assertIsOff()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Loading spinner`() {
        composeTestRule.setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Processing
            ))
        }
        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Edit profile`() {
        composeTestRule.setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "Profile 1",
                    type = ProfileType.Running,
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
        composeTestRule.onNodeWithText("Name").assertTextContains("Profile 1")
        composeTestRule.onNodeWithText("Type").assertTextContains("Running")
        composeTestRule.onNodeWithText("Event type").assertTextContains(EventType.Training.name)
        composeTestRule.onNodeWithText("Course").assertTextContains("Course 1")
        composeTestRule.onNodeWithText("Water").assertTextContains("10")
        composeTestRule.onNodeWithTag("rename_checkbox").assertIsOn()
        composeTestRule.onNodeWithTag("custom_water_checkbox").assertIsOn()
        composeTestRule.onNodeWithTag("feel_and_effort_checkbox").assertIsOn()
        composeTestRule.onNodeWithTag("training_effect_checkbox").assertIsOn()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun `Invalid profile - no name`() {
        composeTestRule.setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "",
                    type = ProfileType.Running,
                    eventType = EventType.Training,
                    course = Course(id = 1, name = "Course 1", distance = 10234.00, type = ActivityType.Running),
                )
            ))
        }
        composeTestRule.onNodeWithText("Name").assertTextContains("")
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Invalid profile - no event type`() {
        composeTestRule.setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "Profile 1",
                    type = ProfileType.Running,
                )
            ))
        }
        composeTestRule.onNodeWithText("Event type").assertTextContains("")
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `No course - type any`() {
        composeTestRule.setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "Profile 1",
                    type = ProfileType.Any,
                    eventType = EventType.Training,
                )
            ))
        }
        composeTestRule.onNodeWithText("Course").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun `No course - type strength`() {
        composeTestRule.setContent {
            ProfileContent(state = ProfileState(
                process = ProcessState.Idle,
                profile = Profile(
                    name = "Profile 1",
                    type = ProfileType.Strength,
                    eventType = EventType.Training
                )
            ))
        }
        composeTestRule.onNodeWithText("Course").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }
}