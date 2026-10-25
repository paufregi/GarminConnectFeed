package paufregi.connectfeed.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import paufregi.connectfeed.presentation.Navigation.HOME
import paufregi.connectfeed.presentation.ui.components.NavigationItem
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Shoe

sealed interface Route : NavKey {
    @Serializable
    data object Login : Route

    @Serializable
    data object QuickEdit : Route

    @Serializable
    data object Edit : Route

    @Serializable
    data object Profiles : Route

    @Serializable
    data object Gears : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data class Profile(val id: Long = 0L) : Route
}

/**
 * Thin wrapper around a Navigation 3 back stack exposing the operations the UI needs.
 * The back stack is a plain [MutableList] so it can be backed by a `NavBackStack`
 * in the app or by an in-memory list in previews/tests.
 */
@Stable
class Navigator(private val backStack: MutableList<NavKey> = mutableStateListOf()) {

    fun navigate(route: Route) {
        if (backStack.lastOrNull() != route) backStack.add(route)
    }

    fun navigateUp() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }
}

@Composable
fun rememberNavigator(backStack: MutableList<NavKey> = remember { mutableStateListOf() }): Navigator =
    remember(backStack) { Navigator(backStack) }

object Navigation {
    const val HOME = 0
    const val PROFILES = 1
    const val GEARS = 2
    const val SETTINGS = 3

    val topItems = listOf(
        NavigationItem(HOME,"Home", Icons.Filled.Home, Route.QuickEdit),
        NavigationItem(PROFILES,"Profiles", Icons.Filled.Tune, Route.Profiles),
        NavigationItem(GEARS,"Gears", Icons.Connect.Shoe, Route.Gears),
    )

    val bottomItems = listOf(
        NavigationItem(SETTINGS, "Settings", Icons.Filled.Settings, Route.Settings),
    )
}

data class HomeNavigation(val barIndex: Int, val menuIndex: Int) {
    companion object {
        val EDIT = HomeNavigation(0, HOME)
        val QUICK_EDIT = HomeNavigation(1, HOME)

        val items = listOf(
            NavigationItem(HOME,"Edit", Icons.Default.Edit, Route.Edit),
            NavigationItem(HOME,"Quick Edit", Icons.Default.EditNote, Route.QuickEdit),
        )
    }
}

