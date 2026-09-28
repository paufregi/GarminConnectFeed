package paufregi.connectfeed.presentation.ui.utils

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import paufregi.connectfeed.presentation.Route

fun NavHostController.isCurrentRoute(route: Route): Boolean =
    currentBackStackEntry?.destination?.hierarchy?.any { destination ->
        destination.hasRoute(route::class)
    } ?: false
