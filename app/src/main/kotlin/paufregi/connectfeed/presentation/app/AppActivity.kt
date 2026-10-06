package paufregi.connectfeed.presentation.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.app.activities.Activities
import paufregi.connectfeed.presentation.app.gears.Gears
import paufregi.connectfeed.presentation.app.login.LoginScreen
import paufregi.connectfeed.presentation.app.profiles.Profiles
import paufregi.connectfeed.presentation.app.settings.SettingsScreen
import paufregi.connectfeed.presentation.ui.components.notification.Notification
import paufregi.connectfeed.presentation.ui.theme.Theme

@AndroidEntryPoint
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
class AppActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            splashScreen.setKeepOnScreenCondition { state is AppState.Loading }

            val backStack = rememberNavBackStack(AppRoute.Activities)
            val navigate: (AppRoute) -> Unit = { backStack.add(it) }

            Theme {
                Notification {
                    when(val s = state) {
                        is AppState.Authenticated -> {
                            key(state) {
                                NavDisplay(
                                    backStack = backStack,
                                    onBack = { backStack.removeLastOrNull() },
                                    entryDecorators = listOf(
                                        rememberSaveableStateHolderNavEntryDecorator(),
                                        rememberViewModelStoreNavEntryDecorator(),
                                    ),
                                    entryProvider = entryProvider {
                                        entry<AppRoute.Activities> { Activities(s.user, navigate) }
                                        entry<AppRoute.Profiles> { Profiles(s.user, navigate) }
                                        entry<AppRoute.Gears> { Gears(s.user, navigate) }
                                        entry<AppRoute.Settings> { SettingsScreen(s.user, navigate) }
                                    }
                                )
                            }
                        }
                        is AppState.NotAuthenticated -> {
                            key(AppState.NotAuthenticated) {
                                LoginScreen()
                            }
                        }
                        is AppState.Loading -> {}
                    }

                }
            }
        }
    }
}
