package paufregi.connectfeed.presentation.main

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.account.AccountScreen
import paufregi.connectfeed.presentation.edit.EditScreen
import paufregi.connectfeed.presentation.login.LoginScreen
import paufregi.connectfeed.presentation.profile.ProfileScreen
import paufregi.connectfeed.presentation.profiles.ProfilesScreen
import paufregi.connectfeed.presentation.quickedit.QuickEditScreen
import paufregi.connectfeed.presentation.syncstrava.SyncStravaScreen
import paufregi.connectfeed.presentation.ui.theme.Theme
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    @Inject
    @Named("StravaAuthUri")
    lateinit var stravaAuthUri: Uri

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val nav = rememberNavController()
            val state by viewModel.state.collectAsStateWithLifecycle()
            installSplashScreen().setKeepOnScreenCondition { state.loggedIn == null }

            if (state.loggedIn == false && state.showLogin != true) {
                viewModel.showLogin()
            }
            Theme {
                NavHost(
                    navController = nav,
                    startDestination = if (state.showLogin == true) Route.Auth else Route.App
                ) {
                    navigation<Route.Auth>(startDestination = Route.Login) {
                        composable<Route.Login> { LoginScreen(viewModel::hideLogin) }
                    }
                    navigation<Route.App>(startDestination = Route.Home) {
                        navigation<Route.Home>(startDestination = Route.QuickEdit) {
                            composable<Route.QuickEdit> { QuickEditScreen(nav = nav) }
                            composable<Route.Edit> { EditScreen(nav = nav) }
                            composable<Route.SyncStrava> { SyncStravaScreen(nav = nav) }
                        }
                        composable<Route.Account> { AccountScreen(stravaAuthUri = stravaAuthUri, nav = nav) }
                        navigation<Route.Profiles>(startDestination = Route.ProfileList) {
                            composable<Route.ProfileList> { ProfilesScreen(nav = nav) }
                            composable<Route.Profile> { ProfileScreen(nav = nav) }
                        }
                    }
                }
            }
        }
    }
}