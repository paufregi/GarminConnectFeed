package paufregi.connectfeed.presentation.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import paufregi.connectfeed.presentation.app.Route

fun NavHostController.isCurrentRoute(route: Route): Boolean =
    currentBackStackEntry?.destination?.hierarchy?.any { destination ->
        destination.hasRoute(route::class)
    } ?: false

@Composable
fun PaddingValues.add(
    other: PaddingValues,
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current

    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
    )
}

@Composable
fun PaddingValues.add(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current

    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + horizontal,
        top = calculateTopPadding() + vertical,
        end = calculateEndPadding(layoutDirection) + horizontal,
        bottom = calculateBottomPadding() + vertical,
    )
}

@Composable
fun PaddingValues.add(
    start: Dp = 0.dp,
    end: Dp = 0.dp,
    top: Dp = 0.dp,
    bottom: Dp = 0.dp,
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current

    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + start,
        top = calculateTopPadding() + top,
        end = calculateEndPadding(layoutDirection) + end,
        bottom = calculateBottomPadding() + bottom,
    )
}