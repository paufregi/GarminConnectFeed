package paufregi.connectfeed.presentation.app.login

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.theme.Theme

@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @Test
    fun `Empty form disables sign in`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                LoginContent(state = LoginState())
            }
        }

        onNodeWithText("Username").assertIsDisplayed()
        onNodeWithText("Password").assertIsDisplayed()
        onNodeWithText("Sign in").assertIsNotEnabled()
    }

    @Test
    fun `Filled form masks password`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                LoginContent(state = LoginState(username = "test-user", password = "test-pass"))
            }
        }

        onNodeWithText("test-user").assertIsDisplayed()
        onNodeWithText("•••••••••").assertIsDisplayed()
        onNodeWithText("Sign in").assertIsEnabled()
    }

    @Test
    fun `Loading status`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                LoginContent(state = LoginState(status = Status.Loading))
            }
        }

        onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `Failure status`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                LoginContent(state = LoginState(status = Status.Failure("Invalid credentials")))
            }
        }

        onNodeWithText("Login failed!").assertIsDisplayed()
        onNodeWithText("Something went wrong: Invalid credentials").assertIsDisplayed()
    }

    @Test
    fun `Success status welcomes user`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                LoginContent(
                    state = LoginState(status = Status.Success, user = User(1, "Test User", "url"))
                )
            }
        }

        onNodeWithText("Welcome Test User").assertIsDisplayed()
        onNodeWithText("OK").assertIsDisplayed()
    }
}
