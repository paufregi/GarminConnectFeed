package paufregi.connectfeed.presentation.app.gears

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.theme.Theme

@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class GearsScreenTest {

    @Test
    fun `Empty list`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                GearsContent(state = GearsState(), user = User.ANONYM)
            }
        }

        onNodeWithText("No gears").assertIsDisplayed()
    }

    @Test
    fun `Gear list displays names and distances`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                GearsContent(
                    state = GearsState(
                        gears = listOf(
                            Gear(id = "gear-1", name = "Daily trainers", type = GearType.Shoe, distance = 1000),
                            Gear(id = "gear-2", name = "Weekend bike", type = GearType.Bike, distance = 25340),
                        )
                    ),
                    user = User.ANONYM,
                )
            }
        }

        onNodeWithText("Daily trainers").assertIsDisplayed()
        onNodeWithText("1 km").assertIsDisplayed()
        onNodeWithText("Weekend bike").assertIsDisplayed()
        onNodeWithText("25 km").assertIsDisplayed()
    }

    @Test
    fun `Loading state hides gear list`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                GearsContent(state = GearsState(loading = true), user = User.ANONYM)
            }
        }

        onNodeWithTag("loading").assertIsDisplayed()
        onNodeWithTag("gear_list").assertDoesNotExist()
    }
}
