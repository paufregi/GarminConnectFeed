package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Ride: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Ride",
        defaultWidth = 24.dp,
        defaultHeight = 16.dp,
        viewportWidth = 24f,
        viewportHeight = 16f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(3.999f, 0f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(1.705f)
            lineToRelative(1.428f, 2.498f)
            lineToRelative(-0.836f, 1.672f)
            arcTo(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 9.899f, 12f)
            lineTo(10.999f, 12f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.868f, -0.504f)
            lineToRelative(3.607f, -6.313f)
            lineToRelative(0.639f, 1.733f)
            arcToRelative(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 1.835f, -0.806f)
            lineTo(16.433f, 2f)
            lineTo(19.499f, 2f)
            arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 1f)
            lineTo(18.999f, 3f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(0.5f)
            arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -5f)
            lineTo(14.999f, 0f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.938f, 1.346f)
            lineTo(14.671f, 3f)
            lineTo(8.579f, 3f)
            lineTo(8.009f, 2f)
            lineTo(8.999f, 2f)
            lineTo(8.999f, 0f)
            close()
            moveTo(8.324f, 6.585f)
            lineTo(10.276f, 10f)
            lineTo(6.617f, 10f)
            close()
            moveTo(11.499f, 8.11f)
            lineTo(9.722f, 5f)
            horizontalLineToRelative(3.554f)
            close()
            moveTo(4.999f, 8f)
            curveToRelative(0.125f, 0f, 0.25f, 0.008f, 0.37f, 0.023f)
            lineToRelative(-1.264f, 2.53f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.999f, 12f)
            horizontalLineToRelative(2.83f)
            arcTo(3.001f, 3.001f, 0f, isMoreThanHalf = true, isPositiveArc = true, 4.999f, 8f)
            close()
            moveTo(16.847f, 8.91f)
            lineToRelative(1.06f, 2.874f)
            lineToRelative(1.876f, -0.691f)
            lineToRelative(-1.132f, -3.073f)
            arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = true, -1.804f, 0.89f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
