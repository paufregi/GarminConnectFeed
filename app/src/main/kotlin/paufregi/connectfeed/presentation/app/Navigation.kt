package paufregi.connectfeed.presentation.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import paufregi.connectfeed.presentation.ui.components.frame.MenuSpec
import paufregi.connectfeed.presentation.ui.components.frame.NavigationItem
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Shoe

sealed interface Route: NavKey {
    @Serializable
    data object Activities : Route

    @Serializable
    data object ActivityList : Route
    @Serializable
    data class ActivityEdit(val id: Long, val stravaId: Long) : Route

    @Serializable
    data class ActivityQuickEdit(val id: Long, val stravaId: Long) : Route

    @Serializable
    data object Profiles : Route

    @Serializable
    data object ProfileList : Route

    @Serializable
    data class ProfileEdit(val id: Long? = null) : Route

    @Serializable
    data object Gears : Route

    @Serializable
    data object Settings : Route
}

object Navigation {
    val menu = MenuSpec(
        topItems = listOf(
            NavigationItem("Activities", Icons.Filled.Home, Route.Activities),
            NavigationItem("Profiles", Icons.Filled.Tune, Route.Profiles),
            NavigationItem("Gears", Icons.Connect.Shoe, Route.Gears),
        ),
        bottomItems = listOf(
            NavigationItem("Settings", Icons.Filled.Settings, Route.Settings),
        ),
    )
}


