package paufregi.connectfeed.presentation.gears

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType

@ExperimentalTestApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class GearsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun `Default values`() {
        composeTestRule.setContent {
            GearsContent(state = GearsState())
        }
        composeTestRule.onNodeWithText("No gears").assertIsDisplayed()
    }

    @Test
    fun `Gear list`() {
        composeTestRule.setContent {
            GearsContent(
                state = GearsState(
                    gears = listOf(
                        Gear(id = "gear-1", name = "Daily trainers", type = GearType.Shoe, distance = 1000),
                        Gear(id = "gear-2", name = "Weekend bike", type = GearType.Bike, distance = 25340),
                    )
                )
            )
        }
        composeTestRule.onNodeWithText("Daily trainers").assertIsDisplayed()
        composeTestRule.onNodeWithText("1 km").assertIsDisplayed()
        composeTestRule.onNodeWithText("Weekend bike").assertIsDisplayed()
        composeTestRule.onNodeWithText("25 km").assertIsDisplayed()
    }
}
