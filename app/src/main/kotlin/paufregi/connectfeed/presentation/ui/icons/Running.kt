package paufregi.connectfeed.presentation.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Running: ImageVector
    get() {
        if (running != null) {
            return running!!
        }
        running = ImageVector.Builder(
            name = "Connect.Running",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            group(
                scaleX = 1f,
                scaleY = -1f,
                translationX = 0f,
                translationY = 409f,
                pivotX = 0f,
                pivotY = 0f,
            ) {
                path(
                    fill = SolidColor(Color(0xFF000000)),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(167f, 102f)
                    lineToRelative(-103f, -64f)
                    lineToRelative(12f, -12f)
                    lineToRelative(125f, 53f)
                    lineToRelative(36f, 45f)
                    lineToRelative(-46f, 36f)
                    close()
                    moveTo(384f, 204f)
                    lineToRelative(83f, 71f)
                    lineToRelative(-12f, 12f)
                    lineToRelative(-71f, -36f)
                    lineToRelative(-71f, 71f)
                    lineToRelative(-95f, 12f)
                    lineToRelative(-59f, -71f)
                    lineToRelative(12f, -12f)
                    lineToRelative(59f, 48f)
                    lineToRelative(40f, -9f)
                    lineToRelative(-28f, -36f)
                    quadToRelative(-30f, -38f, -36f, -48f)
                    reflectiveQuadToRelative(-3f, -16f)
                    quadToRelative(2f, -4f, 6f, -6f)
                    lineToRelative(85f, -70f)
                    lineToRelative(-40f, -123f)
                    lineToRelative(11f, -12f)
                    lineToRelative(87f, 136f)
                    lineToRelative(-59f, 79f)
                    lineToRelative(36f, 48f)
                    close()
                    moveTo(384f, 311f)
                    quadToRelative(14f, 0f, 26f, 8f)
                    reflectiveQuadToRelative(17.5f, 21f)
                    reflectiveQuadToRelative(2.5f, 27.5f)
                    reflectiveQuadToRelative(-12.5f, 24f)
                    reflectiveQuadToRelative(-24f, 12.5f)
                    reflectiveQuadToRelative(-27.5f, -2.5f)
                    reflectiveQuadToRelative(-21f, -17.5f)
                    reflectiveQuadToRelative(-8f, -26f)
                    quadToRelative(0f, -19f, 14f, -33f)
                    reflectiveQuadToRelative(33f, -14f)
                    close()
                }
            }
        }.build()
        return running!!
    }

private var running: ImageVector? = null
