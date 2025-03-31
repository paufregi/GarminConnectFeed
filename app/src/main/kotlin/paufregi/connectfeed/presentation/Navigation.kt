package paufregi.connectfeed.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
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
    data class Profile(val id: Long = 0L) : Route
}

object Navigation {
    const val HOME = 0
    const val PROFILES = 1
    const val ACCOUNT = 2

    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home, Route.Home),
        NavigationItem("Profiles", Icons.Filled.Tune, Route.Profiles),
        NavigationItem("Account", Icons.Filled.AccountCircle, Route.Account),
    )
}

object HomeNavigation {
    const val EDIT = 0
    const val QUICK_EDIT = 1
    const val SYNC_STRAVA = 2

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