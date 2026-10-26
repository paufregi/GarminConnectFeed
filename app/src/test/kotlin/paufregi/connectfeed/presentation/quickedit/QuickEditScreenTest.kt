package paufregi.connectfeed.presentation.quickedit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class QuickEditScreenTest {

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

    private val profiles = listOf(
        Profile(name = "Run basic", type = ActivityType.Running),
        Profile(name = "Bike basic", type = ActivityType.Cycling),
        Profile(name = "Run full", type = ActivityType.Running, customWater = true, gear = true, feelAndEffort = true)
    )

    private val gears = listOf(
        paufregi.connectfeed.core.models.Gear(id = "bike-1", name = "Bike 1", type = paufregi.connectfeed.core.models.GearType.Bike),
        paufregi.connectfeed.core.models.Gear(id = "shoe-1", name = "Shoe 1", type = paufregi.connectfeed.core.models.GearType.Shoe),
    )

    private val incompatibleGears = listOf(
        paufregi.connectfeed.core.models.Gear(id = "shoe-2", name = "Shoe 2", type = paufregi.connectfeed.core.models.GearType.Shoe)
    )

    @Test
    fun `Base screen`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                )
            )
        }

        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Profile").assertIsNotDisplayed()
        onNodeWithText("Description").assertIsNotDisplayed()
        onNodeWithText("Gear").assertIsNotDisplayed()
        onNodeWithText("Water").assertIsNotDisplayed()
        onNodeWithText("0 - None selected").assertIsNotDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Activity selected`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                    activity = activities[0],
                )
            )
        }

        onNodeWithText("Activity").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
        onNodeWithText("Description").assertIsNotDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Activity and profile selected`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                    activity = activities[0],
                    profile = profiles[0],
                    description = "random text",
                )
            )
        }

        onNodeWithText("Activity").assertTextContains(activities[0].name)
        onNodeWithText("Profile").assertTextContains(profiles[0].name)
        onNodeWithText("Description").assertTextContains("random text")
        onNodeWithText("Gear").assertIsNotDisplayed()
        onNodeWithText("Water").assertIsNotDisplayed()
        onNodeWithText("Reset").assertIsEnabled()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Activity and profile selected - with full profile flags`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                    gears = gears,
                    activity = activities[0],
                    profile = profiles[2],
                    gear = gears[1],
                    description = "random text",
                    water = 10,
                    feel = 50f,
                    effort = 80f,
                )
            )
        }

        onNodeWithText("Activity").assertTextContains(activities[0].name)
        onNodeWithText("Profile").assertTextContains(profiles[2].name)
        onNodeWithText("Description").assertTextContains("random text")
        onNodeWithText("Gear").assertTextContains(gears[1].name)
        onNodeWithText("Water").assertTextContains("10")
        onNodeWithText("Normal").assertIsDisplayed()
        onNodeWithText("8 - Very Hard").assertIsDisplayed()
    }

    @Test
    fun `Activity and profile selected - no compatible gears`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                    gears = incompatibleGears,
                    activity = activities[1],
                    profile = profiles[1].copy(gear = true),
                    description = "bike profile",
                )
            )
        }

        onNodeWithText("Activity").assertTextContains(activities[1].name)
        onNodeWithText("Profile").assertTextContains(profiles[1].name)
        onNodeWithText("Description").assertTextContains("bike profile")
        onNodeWithText("Gear").assertIsNotDisplayed()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Activity and profile selected - with Strava`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    stravaActivities = stravaActivities,
                    activity = activities[0],
                    stravaActivity = stravaActivities[0],
                    profiles = profiles,
                    profile = profiles[0],
                )
            )
        }

        onNodeWithText("Activity").assertTextContains(activities[0].name)
        onNodeWithText("Profile").assertTextContains(profiles[0].name)
        onNodeWithText("Description").assertIsDisplayed()
        onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Loading spinner`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Processing,
                )
            )
        }

        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Update success`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Success("Activity updated"),
                )
            )
        }

        onNodeWithTag("status_info_text").assertIsDisplayed()
        onNodeWithText("Activity updated").assertIsDisplayed()
    }

    @Test
    fun `Update failure`() = runComposeUiTest {
        setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Failure("Couldn't update activity"),
                )
            )
        }

        onNodeWithTag("status_info_text").assertIsDisplayed()
        onNodeWithText("Couldn't update activity").assertIsDisplayed()
    }
}