package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Walk: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Walk",
        defaultWidth = 24.dp,
        defaultHeight = 14.dp,
        viewportWidth = 24f,
        viewportHeight = 14f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(9.701f, 0f)
            arcToRelative(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.94f, 0.956f)
            lineToRelative(4.368f, 3.177f)
            lineToRelative(4.595f, 2.12f)
            arcTo(4.127f, 4.127f, 0f, isMoreThanHalf = false, isPositiveArc = true, 24.001f, 10.002f)
            verticalLineToRelative(0.018f)
            curveToRelative(0f, 0.314f, 0.002f, 1.416f, -0.898f, 2.398f)
            curveToRelative(-0.922f, 1.005f, -2.567f, 1.656f, -5.333f, 1.584f)
            lineTo(2.415f, 14.002f)
            arcToRelative(2.414f, 2.414f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.707f, -4.122f)
            lineToRelative(0.086f, -0.086f)
            arcToRelative(0.707f, 0.707f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.207f, -0.5f)
            lineTo(1.001f, 2.547f)
            curveTo(1.001f, 1.693f, 1.694f, 1f, 2.548f, 1f)
            curveToRelative(0.555f, 0f, 1.088f, 0.22f, 1.48f, 0.613f)
            lineTo(5.415f, 3f)
            horizontalLineToRelative(1.699f)
            lineTo(6.551f, 1.316f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.501f, 0f)
            close()
            moveTo(2.394f, 11f)
            curveToRelative(-0.058f, 0.072f, -0.12f, 0.141f, -0.187f, 0.207f)
            lineToRelative(-0.086f, 0.086f)
            arcToRelative(0.414f, 0.414f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.293f, 0.707f)
            lineTo(17.811f, 12f)
            curveToRelative(2.489f, 0.068f, 3.449f, -0.531f, 3.819f, -0.935f)
            curveToRelative(0.372f, -0.406f, 0.372f, -0.847f, 0.372f, -1.062f)
            verticalLineToRelative(-0.002f)
            curveToRelative(0f, -0.35f, -0.086f, -0.687f, -0.242f, -0.985f)
            curveToRelative(-0.4f, 0.059f, -0.638f, 0.278f, -1.05f, 0.691f)
            lineToRelative(-0.027f, 0.026f)
            curveTo(20.171f, 10.243f, 19.416f, 11f, 18.001f, 11f)
            close()
            moveTo(19.949f, 7.693f)
            lineToRelative(-3.092f, -1.426f)
            lineToRelative(-1.08f, 1.62f)
            lineToRelative(-1.664f, -1.11f)
            lineToRelative(1.022f, -1.533f)
            lineToRelative(-0.738f, -0.537f)
            lineToRelative(-1.12f, 1.68f)
            lineToRelative(-1.664f, -1.11f)
            lineToRelative(1.165f, -1.747f)
            lineToRelative(-1.314f, -0.956f)
            arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.7f, 2f)
            horizontalLineToRelative(-0.812f)
            lineToRelative(1f, 3f)
            lineTo(4.588f, 5f)
            lineTo(3.001f, 3.414f)
            lineTo(3.001f, 9f)
            horizontalLineToRelative(15f)
            curveToRelative(0.56f, 0f, 0.803f, -0.218f, 1.293f, -0.707f)
            lineToRelative(0.026f, -0.026f)
            curveToRelative(0.175f, -0.176f, 0.38f, -0.38f, 0.63f, -0.573f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
