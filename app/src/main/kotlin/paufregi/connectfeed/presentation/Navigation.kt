package paufregi.connectfeed.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Tune
import kotlinx.serialization.Serializable
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
    data object SyncStrava : Route

    @Serializable
    data object Profiles : Route

    @Serializable
    data object ProfileList : Route

    @Serializable
    data object Account : Route

    @Serializable
    data object Info : Route

    @Serializable
    data class Profile(val id: Long = 0L) : Route
}

object Navigation {
    const val HOME = 0
    const val PROFILES = 1
    const val ACCOUNT = 2

    const val INFO = 3

    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home, Route.Home),
        NavigationItem("Profiles", Icons.Filled.Tune, Route.Profiles),
        NavigationItem("Account", Icons.Filled.AccountCircle, Route.Account),
        NavigationItem("Info", Icons.Filled.Info, Route.Info),
    )
}

data class HomeNavigation(val barIndex: Int, val menuIndex: Int) {
    companion object {
        val EDIT = HomeNavigation(0, Navigation.HOME)
        val QUICK_EDIT = HomeNavigation(1, Navigation.HOME)
        val SYNC_STRAVA = HomeNavigation(2, Navigation.HOME)

        fun items(hasStrava: Boolean): List<NavigationItem> {
            val list = mutableListOf(
                NavigationItem("Edit", Icons.Default.Edit, Route.Edit),
                NavigationItem("Quick Edit", Icons.Default.EditNote, Route.QuickEdit),
            )
            if (hasStrava) {
                list.add(NavigationItem("Sync Strava", Icons.Default.SyncAlt, Route.SyncStrava))
            }
            return list
        }
    }
}

