package paufregi.connectfeed.presentation.quickedit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

@ExperimentalTestApi
@ExperimentalMaterial3Api
@RunWith(RobolectricTestRunner::class)
class QuickEditScreenTest {

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
    fun `Base screen`() {
        composeTestRule.setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                )
            )
        }

        composeTestRule.onNodeWithText("Activity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Gear").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Water").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("0 - None selected").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Activity selected`() {
        composeTestRule.setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Idle,
                    activities = activities,
                    profiles = profiles,
                    activity = activities[0],
                )
            )
        }

        composeTestRule.onNodeWithText("Activity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun `Activity and profile selected`() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("Activity").assertTextContains(activities[0].name)
        composeTestRule.onNodeWithText("Profile").assertTextContains(profiles[0].name)
        composeTestRule.onNodeWithText("Description").assertTextContains("random text")
        composeTestRule.onNodeWithText("Gear").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Water").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsEnabled()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Activity and profile selected - with full profile flags`() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("Activity").assertTextContains(activities[0].name)
        composeTestRule.onNodeWithText("Profile").assertTextContains(profiles[2].name)
        composeTestRule.onNodeWithText("Description").assertTextContains("random text")
        composeTestRule.onNodeWithText("Gear").assertTextContains(gears[1].name)
        composeTestRule.onNodeWithText("Water").assertTextContains("10")
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
        composeTestRule.onNodeWithText("8 - Very Hard").assertIsDisplayed()
    }

    @Test
    fun `Activity and profile selected - no compatible gears`() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("Activity").assertTextContains(activities[1].name)
        composeTestRule.onNodeWithText("Profile").assertTextContains(profiles[1].name)
        composeTestRule.onNodeWithText("Description").assertTextContains("bike profile")
        composeTestRule.onNodeWithText("Gear").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Activity and profile selected - with Strava`() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("Activity").assertTextContains(activities[0].name)
        composeTestRule.onNodeWithText("Profile").assertTextContains(profiles[0].name)
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun `Loading spinner`() {
        composeTestRule.setContent {
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Processing,
                )
            )
        }

        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Update success`() {
        composeTestRule.setContent {
            QuickEditContent(
                state = QuickEditState(
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
            QuickEditContent(
                state = QuickEditState(
                    process = ProcessState.Failure("Couldn't update activity"),
                )
            )
        }

        composeTestRule.onNodeWithTag("status_info_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Couldn't update activity").assertIsDisplayed()
    }
}