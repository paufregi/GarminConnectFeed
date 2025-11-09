package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Football: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Football",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 0f)
            curveTo(5.372f, 0f, 0f, 5.373f, 0f, 12f)
            reflectiveCurveToRelative(5.372f, 12f, 12f, 12f)
            curveToRelative(6.627f, 0f, 12f, -5.373f, 12f, -12f)
            reflectiveCurveTo(18.627f, 0f, 12f, 0f)
            close()
            moveTo(9.33f, 2.36f)
            arcToRelative(10.043f, 10.043f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.709f, -0.152f)
            lineToRelative(-0.136f, 0.406f)
            lineToRelative(-3.51f, 0.81f)
            close()
            moveTo(7.308f, 3.167f)
            lineTo(8.83f, 4.689f)
            lineTo(7.284f, 7.387f)
            lineTo(4.588f, 8.932f)
            lineToRelative(-1.49f, -1.491f)
            arcToRelative(10.045f, 10.045f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.21f, -4.274f)
            close()
            moveTo(2.318f, 9.489f)
            lineToRelative(1.005f, 1.006f)
            lineToRelative(-0.692f, 3f)
            lineToRelative(-0.494f, 0.164f)
            arcToRelative(10.07f, 10.07f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.18f, -4.17f)
            close()
            moveTo(2.664f, 15.591f)
            lineToRelative(0.056f, -0.018f)
            lineToRelative(3.133f, 3.3f)
            lineToRelative(-0.087f, 0.947f)
            arcToRelative(10.026f, 10.026f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.102f, -4.229f)
            close()
            moveTo(7.666f, 21.015f)
            lineToRelative(0.128f, -1.41f)
            lineToRelative(3.25f, -0.281f)
            lineToRelative(4.045f, 1.156f)
            verticalLineToRelative(1.034f)
            arcToRelative(9.994f, 9.994f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.09f, 0.486f)
            arcToRelative(9.961f, 9.961f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4.333f, -0.985f)
            close()
            moveTo(17.089f, 20.61f)
            verticalLineToRelative(-0.522f)
            lineToRelative(3.046f, -3.655f)
            lineToRelative(0.879f, -0.098f)
            arcToRelative(10.044f, 10.044f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.925f, 4.275f)
            close()
            moveTo(21.748f, 14.241f)
            lineToRelative(-1.43f, 0.159f)
            lineToRelative(-1.095f, -3.284f)
            lineToRelative(0.273f, -3.552f)
            lineToRelative(1.404f, -0.128f)
            arcTo(9.958f, 9.958f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22f, 12f)
            curveToRelative(0f, 0.77f, -0.087f, 1.52f, -0.252f, 2.241f)
            close()
            moveTo(19.636f, 5.543f)
            lineToRelative(-0.756f, 0.068f)
            lineToRelative(-3.039f, -2.486f)
            lineToRelative(0.105f, -0.316f)
            arcToRelative(10.03f, 10.03f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.69f, 2.734f)
            close()
            moveTo(15.738f, 18.585f)
            lineToRelative(-3.32f, -0.948f)
            lineToRelative(1.28f, -3.839f)
            lineToRelative(3.88f, -1.293f)
            lineToRelative(0.921f, 2.766f)
            close()
            moveTo(10.728f, 5.399f)
            lineToRelative(3.693f, -0.852f)
            lineToRelative(3.106f, 2.54f)
            lineToRelative(-0.263f, 3.414f)
            lineToRelative(-4.087f, 1.363f)
            lineToRelative(-3.911f, -3.912f)
            close()
            moveTo(7.43f, 17.629f)
            lineToRelative(-2.976f, -3.136f)
            lineToRelative(0.845f, -3.662f)
            lineTo(7.85f, 9.367f)
            lineToRelative(3.912f, 3.912f)
            lineToRelative(-1.365f, 4.093f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
