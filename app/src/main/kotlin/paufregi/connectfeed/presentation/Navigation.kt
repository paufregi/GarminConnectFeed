package paufregi.connectfeed.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import kotlinx.serialization.Serializable
import paufregi.connectfeed.presentation.ui.components.NavigationItem
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Shoe

sealed interface Route {
    @Serializable
    data object Auth : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object App : Route

    @Serializable
    data object Activities : Route

    @Serializable
    data object Profiles : Route

    @Serializable
    data object Gears : Route

    @Serializable
    data object Settings : Route
}

object Navigation {
    const val ACTIVITIES = 0
    const val PROFILES = 1
    const val GEARS = 2
    const val SETTINGS = 3

    val topItems = listOf(
        NavigationItem(ACTIVITIES,"Activities", Icons.Filled.Home, Route.Activities),
        NavigationItem(PROFILES,"Profiles", Icons.Filled.Tune, Route.Profiles),
        NavigationItem(GEARS,"Gears", Icons.Connect.Shoe, Route.Gears),
    )

    val bottomItems = listOf(
        NavigationItem(SETTINGS, "Settings", Icons.Filled.Settings, Route.Settings),
    )
}
