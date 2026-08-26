package paufregi.connectfeed.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WarningAmber
import kotlinx.serialization.Serializable
import paufregi.connectfeed.presentation.Navigation.HOME
import paufregi.connectfeed.presentation.ui.components.NavigationItem

sealed interface Route {
    @Serializable
    data object Auth : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object App : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object QuickEdit : Route

    @Serializable
    data object Edit : Route

    @Serializable
    data object Profiles : Route

    @Serializable
    data object ProfileList : Route

    @Serializable
    data object Gears : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data class Profile(val id: Long = 0L) : Route
}

object Navigation {
    const val HOME = 0
    const val PROFILES = 1
    const val GEARS = 2
    const val SETTINGS = 3

    val topItems = listOf(
        NavigationItem(HOME,"Home", Icons.Filled.Home, Route.Home),
        NavigationItem(PROFILES,"Profiles", Icons.Filled.Tune, Route.Profiles),
        NavigationItem(GEARS,"Gears", Icons.Filled.WarningAmber, Route.Gears),
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

