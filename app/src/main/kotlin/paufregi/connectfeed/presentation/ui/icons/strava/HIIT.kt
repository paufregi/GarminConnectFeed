package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.HIIT: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.HIIT",
        defaultWidth = 23.dp,
        defaultHeight = 24.dp,
        viewportWidth = 23f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(14.111f, 8.095f)
            arcTo(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 10.5f, 7f)
            verticalLineToRelative(6.5f)
            lineToRelative(4.596f, 4.596f)
            arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.985f, -10f)
            close()
            moveTo(11.5f, 2f)
            horizontalLineToRelative(2f)
            lineTo(13.5f, 0f)
            lineTo(7.5f, 0f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(1.047f)
            curveTo(4.17f, 3.551f, 0f, 8.038f, 0f, 13.5f)
            curveTo(0f, 19.299f, 4.701f, 24f, 10.5f, 24f)
            reflectiveCurveToRelative(10.5f, -4.701f, 10.5f, -10.5f)
            curveToRelative(0f, -2.412f, -0.813f, -4.633f, -2.18f, -6.406f)
            lineToRelative(1.18f, -1.18f)
            lineToRelative(0.793f, 0.793f)
            lineToRelative(1.414f, -1.414f)
            lineToRelative(-3f, -3f)
            lineToRelative(-1.414f, 1.414f)
            lineToRelative(0.793f, 0.793f)
            lineToRelative(-1.132f, 1.132f)
            arcTo(10.457f, 10.457f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11.5f, 3.047f)
            close()
            moveTo(2f, 13.5f)
            arcToRelative(8.5f, 8.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 17f, 0f)
            arcToRelative(8.5f, 8.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -17f, 0f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
