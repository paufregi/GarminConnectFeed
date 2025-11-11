package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Snowboard: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Snowboard",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(15.491f, 1.477f)
            arcToRelative(4.964f, 4.964f, 0f, isMoreThanHalf = true, isPositiveArc = true, 7.02f, 7.02f)
            curveToRelative(-1.12f, 1.12f, -2.435f, 2.257f, -3.715f, 3.364f)
            lineToRelative(-0.017f, 0.014f)
            curveToRelative(-1.299f, 1.123f, -2.562f, 2.215f, -3.626f, 3.279f)
            reflectiveCurveToRelative(-2.156f, 2.327f, -3.278f, 3.626f)
            lineToRelative(-0.015f, 0.016f)
            curveToRelative(-1.107f, 1.28f, -2.243f, 2.595f, -3.363f, 3.715f)
            arcToRelative(4.964f, 4.964f, 0f, isMoreThanHalf = true, isPositiveArc = true, -7.02f, -7.02f)
            curveToRelative(1.12f, -1.12f, 2.435f, -2.256f, 3.715f, -3.363f)
            lineToRelative(0.017f, -0.014f)
            curveToRelative(1.298f, -1.123f, 2.561f, -2.215f, 3.625f, -3.28f)
            curveToRelative(1.064f, -1.063f, 2.157f, -2.326f, 3.28f, -3.625f)
            lineToRelative(0.014f, -0.016f)
            curveToRelative(1.106f, -1.28f, 2.243f, -2.596f, 3.363f, -3.716f)
            close()
            moveTo(21.097f, 2.891f)
            arcToRelative(2.964f, 2.964f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4.192f, 0f)
            curveToRelative(-1.064f, 1.064f, -2.156f, 2.327f, -3.279f, 3.626f)
            lineToRelative(-0.291f, 0.337f)
            curveToRelative(0.407f, -0.063f, 0.836f, 0.03f, 1.192f, 0.284f)
            lineToRelative(2.068f, 1.477f)
            curveToRelative(0.62f, 0.443f, 0.874f, 1.19f, 0.729f, 1.874f)
            lineToRelative(0.147f, -0.127f)
            curveToRelative(1.299f, -1.123f, 2.562f, -2.215f, 3.626f, -3.28f)
            arcToRelative(2.964f, 2.964f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -4.19f)
            close()
            moveTo(10.248f, 10.249f)
            arcToRelative(48.186f, 48.186f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.124f, 1.977f)
            curveToRelative(0.397f, -0.017f, 0.8f, 0.11f, 1.126f, 0.384f)
            lineToRelative(1.924f, 1.629f)
            curveToRelative(0.475f, 0.402f, 0.693f, 0.99f, 0.645f, 1.56f)
            arcToRelative(47.477f, 47.477f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.92f, -2.06f)
            arcToRelative(46.1f, 46.1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.928f, -1.802f)
            arcToRelative(1.83f, 1.83f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.435f, -0.532f)
            lineToRelative(-1.797f, -1.797f)
            arcToRelative(1.62f, 1.62f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.476f, -1.184f)
            arcToRelative(45.066f, 45.066f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.71f, 1.825f)
            close()
            moveTo(6.593f, 13.561f)
            lineToRelative(-0.06f, 0.051f)
            lineToRelative(-0.016f, 0.015f)
            curveToRelative(-1.3f, 1.122f, -2.562f, 2.215f, -3.626f, 3.279f)
            arcToRelative(2.964f, 2.964f, 0f, isMoreThanHalf = true, isPositiveArc = false, 4.191f, 4.191f)
            curveToRelative(1.064f, -1.064f, 2.157f, -2.327f, 3.28f, -3.625f)
            lineToRelative(0.014f, -0.017f)
            lineToRelative(0.014f, -0.016f)
            arcToRelative(1.84f, 1.84f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.81f, -0.607f)
            lineTo(6.953f, 14.908f)
            arcToRelative(1.628f, 1.628f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.359f, -1.347f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
