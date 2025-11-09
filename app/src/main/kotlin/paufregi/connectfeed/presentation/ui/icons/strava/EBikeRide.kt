package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.EBikeRide: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.EBikeRide",
        defaultWidth = 24.dp,
        defaultHeight = 22.dp,
        viewportWidth = 24f,
        viewportHeight = 22f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20.084f, 0.224f)
            lineToRelative(-2.566f, 3.849f)
            arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.416f, 0.777f)
            lineTo(20f, 4.85f)
            verticalLineToRelative(2.349f)
            arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.916f, 0.277f)
            lineToRelative(2.566f, -3.849f)
            arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 23.066f, 2.85f)
            lineTo(21f, 2.85f)
            lineTo(21f, 0.5f)
            arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.916f, -0.277f)
            close()
            moveTo(13.15f, 6.324f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14f, 5.85f)
            horizontalLineToRelative(4f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(-2.382f)
            lineToRelative(2.085f, 4.17f)
            arcTo(5.006f, 5.006f, 0f, isMoreThanHalf = false, isPositiveArc = true, 24f, 16.85f)
            arcToRelative(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -8.085f, -3.935f)
            lineToRelative(-0.707f, -1.413f)
            lineToRelative(-3.34f, 5.844f)
            arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11f, 17.85f)
            lineTo(9.9f, 17.85f)
            arcTo(5.002f, 5.002f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 16.85f)
            arcToRelative(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.297f, -4.83f)
            lineToRelative(0.836f, -1.672f)
            lineToRelative(-0.001f, -0.002f)
            lineToRelative(0.003f, -0.002f)
            lineToRelative(0.247f, -0.494f)
            lineTo(5f, 9.85f)
            lineTo(5f, 7.85f)
            horizontalLineToRelative(5f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(-0.848f)
            lineToRelative(2.348f, 4.11f)
            lineToRelative(2.632f, -4.606f)
            lineToRelative(0.003f, 0.002f)
            lineToRelative(-1.03f, -2.059f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.044f, -0.973f)
            close()
            moveTo(6.617f, 15.85f)
            horizontalLineToRelative(3.659f)
            lineToRelative(-1.952f, -3.415f)
            close()
            moveTo(5.37f, 13.873f)
            arcTo(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 7.83f, 17.85f)
            lineTo(5f, 17.85f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.894f, -1.447f)
            close()
            moveTo(16.84f, 14.767f)
            arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 1.789f, -0.895f)
            lineToRelative(1.712f, 3.425f)
            lineToRelative(-1.79f, 0.895f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
