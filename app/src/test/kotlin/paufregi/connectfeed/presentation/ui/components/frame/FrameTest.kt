package paufregi.connectfeed.presentation.ui.components.frame

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalMaterial3Api
@ExperimentalTestApi
@RunWith(AndroidJUnit4::class)
class FrameTest {

    @Test
    fun `Frame shows title, content and bottom bar`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Frame(
                title = "Title",
                bottomBar = { Text("Bottom") },
                floatingActionButton = { Text("Fab") },
            ) { Text("Content") }
        }

        onNodeWithTag("title").assertTextEquals("Title")
        onNodeWithText("Content").assertIsDisplayed()
        onNodeWithText("Bottom").assertIsDisplayed()
        onNodeWithText("Fab").assertIsDisplayed()
        onNodeWithTag("loading").assertDoesNotExist()
    }

    @Test
    fun `Frame loading replaces content and hides bottom bar and fab`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Frame(
                title = "Title",
                loading = true,
                bottomBar = { Text("Bottom") },
                floatingActionButton = { Text("Fab") },
            ) { Text("Content") }
        }

        onNodeWithTag("title").assertTextEquals("Title")
        onNodeWithTag("loading").assertIsDisplayed()
        onNodeWithText("Content").assertDoesNotExist()
        onNodeWithText("Bottom").assertDoesNotExist()
        onNodeWithText("Fab").assertDoesNotExist()
    }

    @Test
    fun `FormFrame wires back and action buttons`() = runAndroidComposeUiTest<ComponentActivity> {
        var back = false
        var action = false
        setContent {
            FormFrame(
                title = "Form",
                navBack = { back = true },
                action = Icons.Filled.Save,
                onAction = { action = true },
            ) { Text("Content") }
        }

        onNodeWithTag("nav_back").assertIsEnabled().performClick()
        onNodeWithTag("action").assertIsEnabled().performClick()

        assertThat(back).isTrue()
        assertThat(action).isTrue()
    }

    @Test
    fun `FormFrame without action has no action button`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            FormFrame(title = "Form") { Text("Content") }
        }

        onNodeWithTag("nav_back").assertIsDisplayed()
        onNodeWithTag("action").assertDoesNotExist()
    }

    @Test
    fun `FormFrame disables buttons when not enabled`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            FormFrame(title = "Form", action = Icons.Filled.Save, enabled = false) { Text("Content") }
        }

        onNodeWithTag("nav_back").assertIsNotEnabled()
        onNodeWithTag("action").assertIsNotEnabled()
        onNodeWithText("Content").assertIsDisplayed()
    }

    @Test
    fun `FormFrame disables buttons while loading`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            FormFrame(title = "Form", action = Icons.Filled.Save, loading = true) { Text("Content") }
        }

        onNodeWithTag("nav_back").assertIsNotEnabled()
        onNodeWithTag("action").assertIsNotEnabled()
        onNodeWithTag("loading").assertIsDisplayed()
        onNodeWithText("Content").assertDoesNotExist()
    }
}

