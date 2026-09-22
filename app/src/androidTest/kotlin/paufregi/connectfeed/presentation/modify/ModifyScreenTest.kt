package paufregi.connectfeed.presentation.modify

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
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
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState
import kotlin.time.Instant

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class ModifyScreenTest {

    private val activities = listOf(
        Activity(
            id = 1L,
            name = "Running",
            type = ActivityType.Running,
            distance = 10234.00,
            date = Instant.fromEpochMilliseconds(1735693200000),
            trainingEffect = "recovery",
        ),
        Activity(
            id = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            distance = 17803.00,
            date = Instant.fromEpochMilliseconds(1729705968000),
            trainingEffect = "base",
        ),
    )

    private val profiles = listOf(
        Profile(name = "Run basic", type = ActivityType.Running),
        Profile(name = "Run full", type = ActivityType.Running, customWater = true, feelAndEffort = true),
    )

    @Test
    fun `Base screen shows activity list`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ModifyContent(
                state = ModifyState(
                    process = ProcessState.Idle,
                    activities = activities,
                ),
            )
        }

        onNodeWithTag("modify_activity_list").assertIsDisplayed()
        onNodeWithText("Running").assertIsDisplayed()
        onNodeWithText("Cycling").assertIsDisplayed()
        onNodeWithTag("modify_mode_switch").assertIsNotDisplayed()
    }

    @Test
    fun `Selected activity shows manual form`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ModifyContent(
                state = ModifyState(
                    process = ProcessState.Idle,
                    activities = activities,
                    activity = activities[0],
                ),
            )
        }

        onNodeWithTag("modify_detail").assertIsDisplayed()
        onNodeWithTag("modify_mode_switch").assertIsDisplayed()
        onNodeWithText("Name").assertIsDisplayed()
        onNodeWithText("Event type").assertIsDisplayed()
        onNodeWithText("Profile").assertIsNotDisplayed()
        onNodeWithText("Cycling").assertIsNotDisplayed()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Profile mode shows profile form`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ModifyContent(
                state = ModifyState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                    activity = activities[0],
                    mode = ModifyMode.Profile,
                    profile = profiles[1],
                    water = 10,
                    feel = 50f,
                    effort = 80f,
                ),
            )
        }

        onNodeWithText("Profile").assertIsDisplayed()
        onNodeWithText("Water").assertTextContains("10")
        onNodeWithText("Normal").assertIsDisplayed()
        onNodeWithText("8 - Very Hard").assertIsDisplayed()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Profile mode without selected profile disables save`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            ModifyContent(
                state = ModifyState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                    activity = activities[0],
                    mode = ModifyMode.Profile,
                ),
            )
        }

        onNodeWithText("Profile").assertIsDisplayed()
        onNodeWithText("Save").assertIsNotEnabled()
    }
}
