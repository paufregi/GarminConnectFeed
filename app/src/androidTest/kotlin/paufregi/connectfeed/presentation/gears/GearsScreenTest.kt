package paufregi.connectfeed.presentation.gears

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class GearsScreenTest {

    @Test
    fun `Default values`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            GearsContent(state = GearsState())
        }
        onNodeWithText("No gears").assertIsDisplayed()
    }

    @Test
    fun `Gear list`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            GearsContent(
                state = GearsState(
                    gears = listOf(
                        Gear(id = "gear-1", name = "Daily trainers", type = GearType.Shoe, distance = 1000),
                        Gear(id = "gear-2", name = "Weekend bike", type = GearType.Bike, distance = 25340),
                    )
                )
            )
        }
        onNodeWithText("Daily trainers").assertIsDisplayed()
        onNodeWithText("1 km").assertIsDisplayed()
        onNodeWithText("Weekend bike").assertIsDisplayed()
        onNodeWithText("25 km").assertIsDisplayed()
    }
}

