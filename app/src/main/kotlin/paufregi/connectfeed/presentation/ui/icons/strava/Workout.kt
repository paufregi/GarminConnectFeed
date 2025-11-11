package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Workout: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Workout",
        defaultWidth = 22.dp,
        defaultHeight = 22.dp,
        viewportWidth = 22f,
        viewportHeight = 22f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.121f, 0.846f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.25f, -0.024f)
            lineTo(6.658f, 3.858f)
            arcToRelative(0.429f, 0.429f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.346f, 0.176f)
            lineTo(0f, 4.034f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(6.312f)
            curveToRelative(0.776f, 0f, 1.505f, -0.37f, 1.963f, -0.998f)
            lineToRelative(2.212f, -3.036f)
            lineToRelative(4.362f, 6.179f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.29f, -0.032f)
            lineToRelative(1.303f, -1.925f)
            arcTo(0.429f, 0.429f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19.797f, 6.035f)
            lineTo(22f, 6.035f)
            lineTo(22f, 4.035f)
            horizontalLineToRelative(-2.204f)
            curveToRelative(-0.805f, 0f, -1.559f, 0.4f, -2.01f, 1.067f)
            lineToRelative(-1.303f, 1.925f)
            close()
            moveTo(6.87f, 7.822f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.251f, 0.024f)
            lineToRelative(4.362f, 6.18f)
            lineToRelative(1.98f, -2.925f)
            arcTo(2.429f, 2.429f, 0f, isMoreThanHalf = false, isPositiveArc = true, 18.472f, 10.035f)
            lineTo(22f, 10.035f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(-3.527f)
            arcToRelative(0.429f, 0.429f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.354f, 0.188f)
            lineToRelative(-1.98f, 2.925f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.29f, 0.032f)
            lineToRelative(-4.362f, -6.18f)
            lineToRelative(-1.484f, 2.037f)
            arcToRelative(2.429f, 2.429f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.962f, 0.998f)
            lineTo(0f, 12.035f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(5.04f)
            arcToRelative(0.429f, 0.429f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.347f, -0.176f)
            close()
            moveTo(5.37f, 13.822f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.251f, 0.024f)
            lineToRelative(4.362f, 6.18f)
            lineToRelative(1.302f, -1.925f)
            arcTo(2.429f, 2.429f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16.296f, 17.035f)
            lineTo(22f, 17.035f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(-5.704f)
            arcToRelative(0.429f, 0.429f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.354f, 0.188f)
            lineToRelative(-1.303f, 1.925f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.29f, 0.032f)
            lineToRelative(-4.362f, -6.18f)
            lineToRelative(-2.212f, 3.037f)
            arcToRelative(2.429f, 2.429f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.963f, 0.998f)
            lineTo(0f, 19.035f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(2.812f)
            arcToRelative(0.429f, 0.429f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.346f, -0.176f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
