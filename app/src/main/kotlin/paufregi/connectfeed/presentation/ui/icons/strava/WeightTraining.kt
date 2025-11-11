package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.WeightTraining: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.WeightTraining",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(21.3f, 13.814f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.83f, 0f)
            lineToRelative(-2.297f, -2.298f)
            lineToRelative(-4.657f, 4.657f)
            lineToRelative(2.298f, 2.298f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 2.829f)
            lineTo(12.4f, 22.714f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.829f, 0f)
            lineToRelative(-0.707f, -0.707f)
            lineToRelative(-1.207f, 1.207f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.828f, 0f)
            lineTo(0.586f, 18.971f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -2.828f)
            lineToRelative(1.207f, -1.207f)
            lineToRelative(-0.707f, -0.707f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -2.829f)
            lineTo(2.5f, 9.986f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.829f, 0f)
            lineToRelative(2.298f, 2.298f)
            lineToRelative(4.657f, -4.657f)
            lineToRelative(-2.298f, -2.298f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -2.828f)
            lineTo(11.4f, 1.086f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.828f, 0f)
            lineToRelative(0.708f, 0.707f)
            lineTo(16.143f, 0.586f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.828f, 0f)
            lineToRelative(4.243f, 4.243f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 2.828f)
            lineToRelative(-1.208f, 1.208f)
            lineToRelative(0.707f, 0.707f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 2.828f)
            close()
            moveTo(12.813f, 2.501f)
            lineTo(11.4f, 3.915f)
            lineToRelative(8.485f, 8.485f)
            lineToRelative(1.414f, -1.414f)
            lineToRelative(-2.121f, -2.121f)
            lineToRelative(2.621f, -2.622f)
            lineTo(17.557f, 2.001f)
            lineTo(16.35f, 3.208f)
            lineToRelative(2.828f, 2.828f)
            lineToRelative(-1.414f, 1.414f)
            close()
            moveTo(13.697f, 9.041f)
            lineTo(9.04f, 13.698f)
            lineToRelative(1.06f, 1.06f)
            lineToRelative(4.658f, -4.656f)
            close()
            moveTo(12.4f, 19.885f)
            lineTo(3.915f, 11.401f)
            lineTo(2.5f, 12.814f)
            lineToRelative(4.95f, 4.95f)
            lineToRelative(-1.414f, 1.414f)
            lineToRelative(-2.828f, -2.828f)
            lineTo(2f, 17.557f)
            lineToRelative(4.243f, 4.243f)
            lineToRelative(2.621f, -2.622f)
            lineToRelative(2.122f, 2.122f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
