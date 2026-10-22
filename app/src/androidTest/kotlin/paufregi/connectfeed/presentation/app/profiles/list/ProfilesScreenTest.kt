package paufregi.connectfeed.presentation.app.profiles.list

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.theme.Theme

@ExperimentalMaterial3Api
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class ProfilesScreenTest {

    @Test
    fun `Empty list`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                ProfilesContent(
                    state = ProfilesState(isLoading = false),
                    user = User.ANONYM,
                )
            }
        }

        onNodeWithText("No profiles").assertIsDisplayed()
    }

    @Test
    fun `Profile list displays profiles`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                ProfilesContent(
                    state = ProfilesState(
                        isLoading = false,
                        profiles = listOf(
                            Profile(id = 1, name = "Morning run", type = ActivityType.Running),
                            Profile(id = 2, name = "Evening ride", type = ActivityType.Cycling),
                        ),
                    ),
                    user = User.ANONYM,
                )
            }
        }

        onNodeWithText("Morning run").assertIsDisplayed()
        onNodeWithText("Evening ride").assertIsDisplayed()
    }

    @Test
    fun `Selecting a profile opens its id`() = runAndroidComposeUiTest<ComponentActivity> {
        var selectedId: Long? = null
        setContent {
            Theme {
                ProfilesContent(
                    state = ProfilesState(
                        isLoading = false,
                        profiles = listOf(Profile(id = 7, name = "Long run", type = ActivityType.Running)),
                    ),
                    user = User.ANONYM,
                    onOpen = { selectedId = it },
                )
            }
        }

        onNodeWithTag("profile_7").performClick()

        assertEquals(7L, selectedId)
    }

    @Test
    fun `Loading state hides profile list`() = runAndroidComposeUiTest<ComponentActivity> {
        setContent {
            Theme {
                ProfilesContent(state = ProfilesState(isLoading = true), user = User.ANONYM)
            }
        }

        onNodeWithTag("loading").assertIsDisplayed()
        onNodeWithTag("profile_list_content").assertDoesNotExist()
    }
}
