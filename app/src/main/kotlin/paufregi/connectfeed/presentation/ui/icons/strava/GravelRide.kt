package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.GravelRide: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.GravelRide",
        defaultWidth = 24.dp,
        defaultHeight = 19.dp,
        viewportWidth = 24f,
        viewportHeight = 19f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(9f, 0f)
            lineTo(4f, 0f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(1.705f)
            lineToRelative(2.666f, 4.665f)
            lineToRelative(-0.374f, 0.333f)
            arcTo(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 9.9f, 12f)
            lineTo(11f, 12f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.868f, -0.504f)
            lineToRelative(3.607f, -6.313f)
            lineToRelative(0.639f, 1.733f)
            arcToRelative(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 1.835f, -0.806f)
            lineTo(16.434f, 2f)
            lineTo(18f, 2f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, 1f)
            horizontalLineToRelative(2f)
            arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3f, -3f)
            horizontalLineToRelative(-3f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.938f, 1.346f)
            lineTo(14.672f, 3f)
            lineTo(8.58f, 3f)
            lineTo(8.01f, 2f)
            lineTo(9f, 2f)
            close()
            moveTo(7.63f, 10f)
            lineToRelative(1.755f, -1.56f)
            lineToRelative(0.892f, 1.56f)
            close()
            moveTo(13.277f, 5f)
            lineTo(11.5f, 8.11f)
            lineTo(9.723f, 5f)
            close()
            moveTo(5f, 8f)
            curveToRelative(0.526f, 0f, 1.02f, 0.135f, 1.45f, 0.373f)
            lineToRelative(-2.114f, 1.88f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5f, 12f)
            horizontalLineToRelative(2.83f)
            arcTo(3.001f, 3.001f, 0f, isMoreThanHalf = true, isPositiveArc = true, 5f, 8f)
            close()
            moveTo(16.848f, 8.91f)
            lineToRelative(1.06f, 2.874f)
            lineToRelative(1.876f, -0.691f)
            lineToRelative(-1.132f, -3.073f)
            arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = true, -1.804f, 0.89f)
            close()
            moveTo(13f, 16.586f)
            lineToRelative(-1.293f, -1.293f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.414f, 0f)
            lineTo(8.586f, 17f)
            lineTo(0f, 17f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(9f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.707f, -0.293f)
            lineTo(11f, 17.414f)
            lineToRelative(1.293f, 1.293f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.414f, 0f)
            lineToRelative(0.793f, -0.793f)
            lineToRelative(0.793f, 0.793f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 16f, 19f)
            horizontalLineToRelative(8f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(-7.586f)
            lineToRelative(-1.207f, -1.207f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.414f, 0f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
