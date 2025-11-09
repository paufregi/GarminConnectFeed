package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Swim: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Swim",
        defaultWidth = 24.dp,
        defaultHeight = 18.dp,
        viewportWidth = 24f,
        viewportHeight = 18f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(16.314f, 0.433f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.647f, 0f)
            curveTo(13.913f, 1.53f, 13.038f, 2f, 11.824f, 2f)
            reflectiveCurveToRelative(-2.089f, -0.47f, -2.842f, -1.567f)
            lineTo(7.334f, 1.567f)
            curveTo(8.43f, 3.16f, 9.9f, 4f, 11.824f, 4f)
            curveToRelative(1.478f, 0f, 2.688f, -0.495f, 3.667f, -1.447f)
            curveTo(16.469f, 3.505f, 17.679f, 4f, 19.157f, 4f)
            curveToRelative(1.925f, 0f, 3.395f, -0.839f, 4.491f, -2.433f)
            lineToRelative(-1.648f, -1.134f)
            curveTo(21.247f, 1.53f, 20.371f, 2f, 19.157f, 2f)
            reflectiveCurveToRelative(-2.088f, -0.47f, -2.842f, -1.567f)
            close()
            moveTo(8.158f, 7f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.824f, 0.433f)
            curveTo(9.734f, 8.53f, 10.61f, 9f, 11.824f, 9f)
            curveToRelative(1.214f, 0f, 2.09f, -0.47f, 2.843f, -1.567f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.648f, 0f)
            curveTo(17.068f, 8.53f, 17.943f, 9f, 19.157f, 9f)
            curveToRelative(1.214f, 0f, 2.09f, -0.47f, 2.843f, -1.567f)
            lineToRelative(1.648f, 1.134f)
            curveTo(22.552f, 10.16f, 21.082f, 11f, 19.158f, 11f)
            curveToRelative(-1.479f, 0f, -2.689f, -0.495f, -3.667f, -1.447f)
            curveTo(14.512f, 10.505f, 13.302f, 11f, 11.824f, 11f)
            curveToRelative(-1.478f, 0f, -2.688f, -0.495f, -3.667f, -1.447f)
            curveTo(7.179f, 10.505f, 5.969f, 11f, 4.491f, 11f)
            curveToRelative(-1.925f, 0f, -3.395f, -0.839f, -4.491f, -2.433f)
            lineToRelative(1.648f, -1.134f)
            curveTo(2.402f, 8.53f, 3.277f, 9f, 4.491f, 9f)
            reflectiveCurveToRelative(2.089f, -0.47f, 2.842f, -1.567f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.158f, 7f)
            close()
            moveTo(8.158f, 14f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.824f, 0.433f)
            curveTo(9.734f, 15.53f, 10.61f, 16f, 11.824f, 16f)
            curveToRelative(1.214f, 0f, 2.09f, -0.47f, 2.843f, -1.567f)
            lineToRelative(1.648f, 1.133f)
            curveTo(15.219f, 17.161f, 13.749f, 18f, 11.824f, 18f)
            curveToRelative(-1.479f, 0f, -2.689f, -0.495f, -3.668f, -1.447f)
            curveTo(7.179f, 17.505f, 5.969f, 18f, 4.491f, 18f)
            curveToRelative(-1.925f, 0f, -3.395f, -0.839f, -4.491f, -2.434f)
            lineToRelative(1.648f, -1.133f)
            curveTo(2.402f, 15.53f, 3.277f, 16f, 4.491f, 16f)
            reflectiveCurveToRelative(2.089f, -0.47f, 2.842f, -1.567f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.157f, 14f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
