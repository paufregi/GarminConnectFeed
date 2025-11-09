package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Yoga: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Yoga",
        defaultWidth = 24.dp,
        defaultHeight = 23.dp,
        viewportWidth = 24f,
        viewportHeight = 23f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(16.22f, 23f)
            lineTo(5.5f, 23f)
            arcToRelative(5.978f, 5.978f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.265f, -0.443f)
            curveTo(1.928f, 22.021f, 0.878f, 21.03f, 0.354f, 19.766f)
            arcToRelative(4.593f, 4.593f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.161f, -3.881f)
            lineToRelative(5.991f, -12.98f)
            arcToRelative(0.983f, 0.983f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.02f, -0.042f)
            curveTo(7.159f, 1.646f, 8.289f, 0.737f, 9.634f, 0.296f)
            arcToRelative(6.02f, 6.02f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.132f, 0.147f)
            curveToRelative(1.307f, 0.536f, 2.357f, 1.527f, 2.881f, 2.791f)
            arcToRelative(4.592f, 4.592f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.161f, 3.881f)
            lineTo(16.076f, 8f)
            horizontalLineToRelative(5.861f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.816f, 2.838f)
            lineToRelative(-4.809f, 10.42f)
            arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16.22f, 23f)
            close()
            moveTo(13.006f, 2.293f)
            arcToRelative(4.02f, 4.02f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.75f, -0.096f)
            curveToRelative(-0.887f, 0.29f, -1.573f, 0.867f, -1.944f, 1.569f)
            lineTo(3.957f, 13.2f)
            arcToRelative(6.02f, 6.02f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.808f, 0.243f)
            curveToRelative(1.093f, 0.448f, 2.007f, 1.216f, 2.584f, 2.195f)
            lineToRelative(4.329f, -9.38f)
            lineToRelative(0.02f, -0.043f)
            arcToRelative(2.594f, 2.594f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.1f, -2.215f)
            curveToRelative(-0.3f, -0.727f, -0.93f, -1.353f, -1.792f, -1.707f)
            close()
            moveTo(8.983f, 17.708f)
            arcTo(2.63f, 2.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.8f, 17f)
            curveToRelative(-0.302f, -0.727f, -0.932f, -1.354f, -1.793f, -1.707f)
            arcToRelative(4.02f, 4.02f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.75f, -0.096f)
            curveToRelative(-0.89f, 0.291f, -1.579f, 0.872f, -1.949f, 1.577f)
            arcTo(2.594f, 2.594f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.201f, 19f)
            curveToRelative(0.302f, 0.727f, 0.932f, 1.354f, 1.793f, 1.707f)
            curveToRelative(0.473f, 0.194f, 0.987f, 0.293f, 1.506f, 0.293f)
            horizontalLineToRelative(10.72f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.908f, -0.58f)
            lineTo(21.938f, 10f)
            horizontalLineToRelative(-6.785f)
            lineToRelative(-3.516f, 7.619f)
            arcToRelative(4.099f, 4.099f, 0f, isMoreThanHalf = false, isPositiveArc = true, -7.388f, 0.115f)
            lineToRelative(-0.143f, -0.287f)
            lineToRelative(1.788f, -0.894f)
            lineToRelative(0.144f, 0.287f)
            arcToRelative(2.099f, 2.099f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.945f, 0.868f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
