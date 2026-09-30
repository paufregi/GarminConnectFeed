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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.app.activities.list.ActivitiesScreen
import paufregi.connectfeed.presentation.app.gears.GearsScreen
import paufregi.connectfeed.presentation.app.login.LoginScreen
import paufregi.connectfeed.presentation.app.profiles.Profiles
import paufregi.connectfeed.presentation.app.settings.SettingsScreen
import paufregi.connectfeed.presentation.ui.components.frame.Frame
import paufregi.connectfeed.presentation.ui.models.AuthState
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
            splashScreen.setKeepOnScreenCondition { state == AuthState.Loading }

            val backStack = rememberNavBackStack(Route.Activities as NavKey)

            Theme {
                when(state) {
                    is AuthState.Authenticated -> {
                        key(AuthState.Authenticated) {
                            Frame(
                                menuSpec = Navigation.menu,
                                currentRoute = backStack.lastOrNull() as? Route,
                                navigate = { route -> backStack.add(route) }
                            ) { padding ->
                                NavDisplay(
                                    backStack = backStack,
                                    onBack = { backStack.removeLastOrNull() },
                                    entryDecorators = listOf(
                                        rememberSaveableStateHolderNavEntryDecorator(),
                                        rememberViewModelStoreNavEntryDecorator(),
                                    ),
                                    entryProvider = entryProvider {
                                        entry<Route.Activities> { ActivitiesScreen(padding) }
                                        entry<Route.Profiles> { Profiles( padding) }
                                        entry<Route.Gears> { GearsScreen(padding) }
                                        entry<Route.Settings> { SettingsScreen(padding) }
                                    }
                                )
                            }
                        }
                    }
                    is AuthState.NotAuthenticated -> {
                        key(AuthState.NotAuthenticated) {
                            LoginScreen()
                        }
                    }
                    is AuthState.Loading -> {}
                }
            }
        }
    }
}
