package paufregi.connectfeed.presentation.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
