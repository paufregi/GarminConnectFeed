package paufregi.connectfeed.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.AuthRoute
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.edit.EditScreen
import paufregi.connectfeed.presentation.gears.GearsScreen
import paufregi.connectfeed.presentation.login.LoginScreen
import paufregi.connectfeed.presentation.profile.ProfileScreen
import paufregi.connectfeed.presentation.profiles.ProfilesScreen
import paufregi.connectfeed.presentation.quickedit.QuickEditScreen
import paufregi.connectfeed.presentation.rememberNavigator
import paufregi.connectfeed.presentation.settings.SettingsScreen
import paufregi.connectfeed.presentation.ui.theme.Theme

@AndroidEntryPoint
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { viewModel.state.value.loggedIn == null }

        enableEdgeToEdge()
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()

            state.loggedIn?.let { loggedIn ->
                Theme {
                    // Two fully independent navigation hosts: when auth state flips the
                    // previous host (and its back stack) leaves composition entirely, so
                    // there is no way to navigate - or press back - between the two flows.
                    when (loggedIn) {
                        true -> AppNavigation()
                        false -> AuthNavigation()
                    }
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun AuthNavigation() {
    val backStack = rememberNavBackStack(AuthRoute.Login)

    NavDisplay(
        backStack = backStack,
        // Single-entry stack: back is never intercepted here, the system handles it.
        onBack = {},
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<AuthRoute.Login> { LoginScreen() }
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
private fun AppNavigation() {
    val backStack = rememberNavBackStack(Route.QuickEdit)
    val nav = rememberNavigator(backStack)

    NavDisplay(
        backStack = backStack,
        onBack = { nav.navigateUp() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.QuickEdit> { QuickEditScreen(nav = nav) }
            entry<Route.Edit> { EditScreen(nav = nav) }
            entry<Route.Profiles> { ProfilesScreen(nav = nav) }
            entry<Route.Profile> { ProfileScreen(id = it.id, nav = nav) }
            entry<Route.Gears> { GearsScreen(nav = nav) }
            entry<Route.Settings> { SettingsScreen(nav = nav) }
        }
    )
}