package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.MountainBikeRide: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.MountainBikeRide",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(13.25f, 0.439f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.5f, 0f)
            lineToRelative(6.875f, 5.5f)
            lineToRelative(-1.25f, 1.562f)
            lineToRelative(-6.875f, -5.5f)
            lineTo(11.1f, 4.72f)
            lineToRelative(1.525f, 1.219f)
            lineToRelative(-1.25f, 1.562f)
            lineTo(7f, 4f)
            lineToRelative(-4.375f, 3.5f)
            lineToRelative(-1.25f, -1.562f)
            lineToRelative(4.376f, -3.5f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.498f, 0f)
            lineToRelative(1.251f, 1f)
            close()
            moveTo(19.265f, 9.352f)
            lineToRelative(-2.904f, 0.968f)
            lineToRelative(1.563f, 3.516f)
            arcToRelative(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -1.827f, 0.813f)
            lineToRelative(-0.709f, -1.593f)
            lineToRelative(-3.52f, 6.16f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11f, 19.72f)
            lineTo(9.9f, 19.72f)
            arcTo(5.002f, 5.002f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.72f)
            arcToRelative(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.886f, -4.632f)
            lineToRelative(0.089f, -0.133f)
            lineTo(5.034f, 10.72f)
            lineTo(3f, 10.72f)
            lineTo(3f, 8.72f)
            horizontalLineToRelative(5f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(-0.634f)
            lineToRelative(1.397f, 2.327f)
            lineToRelative(5.655f, -2.175f)
            lineToRelative(-0.332f, -0.746f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.598f, -1.355f)
            lineToRelative(3.949f, -1.316f)
            close()
            moveTo(5.737f, 15.811f)
            arcTo(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 7.83f, 19.72f)
            lineTo(5f, 19.72f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.832f, -1.555f)
            close()
            moveTo(9.234f, 17.72f)
            lineToRelative(-1.12f, -1.868f)
            lineTo(6.868f, 17.72f)
            close()
            moveTo(10.979f, 16.74f)
            lineToRelative(1.76f, -3.08f)
            lineToRelative(-2.932f, 1.128f)
            close()
            moveTo(16.938f, 16.541f)
            arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 1.828f, -0.812f)
            lineToRelative(1.554f, 3.498f)
            lineToRelative(-1.828f, 0.813f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
