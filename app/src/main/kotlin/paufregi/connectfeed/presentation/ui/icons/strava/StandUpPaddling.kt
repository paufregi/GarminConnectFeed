package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.StandUpPaddling: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.StandUpPaddling",
        defaultWidth = 20.dp,
        defaultHeight = 24.dp,
        viewportWidth = 20f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(14.736f, 0.067f)
            arcToRelative(1.07f, 1.07f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.744f, 0f)
            curveToRelative(0.339f, 0.125f, 1.52f, 0.66f, 2.607f, 2.308f)
            curveToRelative(1.083f, 1.643f, 2.02f, 4.313f, 2.02f, 8.622f)
            curveToRelative(0f, 7.455f, -2.762f, 11.934f, -3.134f, 12.505f)
            arcToRelative(1.093f, 1.093f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.918f, 0.495f)
            horizontalLineToRelative(-1.894f)
            curveToRelative(-0.335f, 0f, -0.697f, -0.156f, -0.918f, -0.495f)
            curveTo(12.87f, 22.932f, 10.108f, 18.452f, 10.108f, 10.997f)
            curveToRelative(0f, -4.309f, 0.938f, -6.98f, 2.021f, -8.622f)
            curveTo(13.216f, 0.727f, 14.397f, 0.192f, 14.736f, 0.068f)
            close()
            moveTo(15.108f, 2.115f)
            curveToRelative(-0.31f, 0.2f, -0.809f, 0.603f, -1.31f, 1.362f)
            curveTo(12.983f, 4.715f, 12.108f, 6.977f, 12.108f, 10.997f)
            curveToRelative(0f, 5.908f, 1.86f, 9.758f, 2.562f, 11f)
            horizontalLineToRelative(0.875f)
            curveTo(16.248f, 20.756f, 18.108f, 16.905f, 18.108f, 10.997f)
            curveToRelative(0f, -4.02f, -0.874f, -6.28f, -1.691f, -7.52f)
            curveToRelative(-0.5f, -0.759f, -0.998f, -1.163f, -1.31f, -1.362f)
            close()
            moveTo(2.097f, 21.512f)
            curveToRelative(0.952f, 0.347f, 1.794f, 0.21f, 2.35f, 0.016f)
            curveToRelative(0.772f, -0.268f, 1.172f, -0.923f, 1.36f, -1.438f)
            lineToRelative(0.453f, -1.244f)
            arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.2f, -4.588f)
            lineToRelative(-0.15f, -0.318f)
            lineToRelative(3.991f, -10.964f)
            lineToRelative(0.94f, 0.342f)
            lineToRelative(0.684f, -1.88f)
            lineTo(7.766f, 0.07f)
            lineToRelative(-0.684f, 1.88f)
            lineToRelative(0.94f, 0.342f)
            lineTo(4.027f, 13.267f)
            lineToRelative(-0.303f, 0.141f)
            arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.103f, 3.386f)
            lineToRelative(-0.452f, 1.244f)
            curveToRelative(-0.188f, 0.515f, -0.302f, 1.274f, 0.117f, 1.975f)
            arcToRelative(3.474f, 3.474f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.81f, 1.5f)
            close()
            moveTo(3.818f, 19.611f)
            arcToRelative(0.152f, 0.152f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.03f, 0.028f)
            curveToRelative(-0.249f, 0.087f, -0.6f, 0.142f, -1.007f, -0.006f)
            arcToRelative(1.475f, 1.475f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.777f, -0.643f)
            arcToRelative(0.148f, 0.148f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.004f, -0.041f)
            curveToRelative(0f, -0.048f, 0.011f, -0.126f, 0.048f, -0.227f)
            lineToRelative(0.453f, -1.244f)
            arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.844f, -2.144f)
            arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.035f, 2.828f)
            lineTo(3.928f, 19.407f)
            arcToRelative(0.688f, 0.688f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.11f, 0.205f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
