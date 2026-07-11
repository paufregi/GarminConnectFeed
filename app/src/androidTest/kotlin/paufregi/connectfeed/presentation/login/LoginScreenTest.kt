package paufregi.connectfeed.presentation.login

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltAndroidTest
@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @Test
    fun `Form - empty` () = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            LoginContent(state = LoginState())
        }
        onNodeWithText("Username").assertTextContains("")
        onNodeWithText("Password").assertTextContains("")
        onNodeWithText("Sign in").assertIsNotEnabled()
    }

    @Test
    fun `Form - filled` () = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            LoginContent(state = LoginState(username = "userTest", password = "passTest"))
        }
        onNodeWithText("Username").assertTextContains("userTest")
        onNodeWithText("Password").assertTextContains("••••••••")
        onNodeWithText("Sign in").assertIsEnabled()
    }

    @Test
    fun `Form - filled with visible password` () = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            LoginContent(state = LoginState(username = "userTest", password = "passTest", showPassword = true))
        }
        onNodeWithText("Username").assertTextContains("userTest")
        onNodeWithText("Password").assertTextContains("passTest")
        onNodeWithText("Sign in").assertIsEnabled()
    }

    @Test
    fun `Loading spinning` () = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            LoginContent(state = LoginState(process = ProcessState.Processing))
        }
        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Sign in - failure` () = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            LoginContent(state = LoginState(process = ProcessState.Failure("error")))
        }
        onNodeWithText("error").assertIsDisplayed()
    }
}