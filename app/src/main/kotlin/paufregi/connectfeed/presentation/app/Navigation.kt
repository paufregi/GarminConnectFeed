package paufregi.connectfeed.presentation.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Shoe

sealed interface AppRoute: NavKey {
    @Serializable
    data object Activities : AppRoute

    @Serializable
    data object Profiles : AppRoute

    @Serializable
    data object Gears : AppRoute

    @Serializable
    data object Settings : AppRoute
}

sealed interface ActivitiesRoute: NavKey {
    @Serializable
    data object List : ActivitiesRoute

    @Serializable
    data class Edit(val id: Long, val stravaId: Long?) : ActivitiesRoute

    @Serializable
    data class QuickEdit(val id: Long, val stravaId: Long?) : ActivitiesRoute
}

sealed interface ProfilesRoute: NavKey {
    @Serializable
    data object List : ProfilesRoute

    @Serializable
    data class Edit(val id: Long? = null) : ProfilesRoute
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: AppRoute,
)

data class MenuSpec(
    val topItems: List<NavigationItem>,
    val bottomItems: List<NavigationItem>,
)

object Navigation {
    val menu = MenuSpec(
        topItems = listOf(
            NavigationItem("Activities", Icons.Filled.Home, AppRoute.Activities),
            NavigationItem("Profiles", Icons.Filled.Tune, AppRoute.Profiles),
            NavigationItem("Gears", Icons.Connect.Shoe, AppRoute.Gears),
        ),
        bottomItems = listOf(
            NavigationItem("Settings", Icons.Filled.Settings, AppRoute.Settings),
        ),
    )
}


