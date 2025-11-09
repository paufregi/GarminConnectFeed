package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Windsurf: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Windsurf",
        defaultWidth = 22.dp,
        defaultHeight = 23.dp,
        viewportWidth = 22f,
        viewportHeight = 23f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.546f, 0f)
            horizontalLineToRelative(2.092f)
            curveToRelative(0.459f, 0f, 0.976f, 0.162f, 1.358f, 0.584f)
            curveTo(16.89f, 1.573f, 19f, 4.601f, 19f, 11f)
            curveToRelative(0f, 3.887f, -0.778f, 6.767f, -1.576f, 8.695f)
            curveToRelative(-0.207f, 0.5f, -0.414f, 0.934f, -0.61f, 1.305f)
            lineTo(22f, 21f)
            verticalLineToRelative(2f)
            lineTo(3f, 23f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(11.494f)
            arcToRelative(8.203f, 8.203f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.258f, -0.409f)
            lineTo(2.814f, 16f)
            lineTo(0f, 16f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(2.454f)
            lineTo(10.864f, 0.918f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12.546f, 0f)
            close()
            moveTo(8.386f, 16f)
            lineToRelative(7.248f, 2.788f)
            curveToRelative(0.303f, -0.755f, 0.607f, -1.682f, 0.85f, -2.788f)
            close()
            moveTo(16.823f, 14f)
            curveToRelative(0.111f, -0.911f, 0.177f, -1.91f, 0.177f, -3f)
            horizontalLineToRelative(-7f)
            lineTo(10f, 9f)
            horizontalLineToRelative(6.921f)
            arcToRelative(20.959f, 20.959f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.106f, -1f)
            lineTo(12f, 8f)
            lineTo(12f, 6f)
            horizontalLineToRelative(4.44f)
            curveToRelative(-0.09f, -0.36f, -0.188f, -0.692f, -0.29f, -1f)
            lineTo(14f, 5f)
            lineTo(14f, 3f)
            horizontalLineToRelative(1.272f)
            arcToRelative(6.548f, 6.548f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.694f, -1f)
            horizontalLineToRelative(-2.032f)
            lineTo(4.832f, 14f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
