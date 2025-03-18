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

val ConnectIcons.OpenWaterSwimming: ImageVector
    get() {
        if (openWaterSwimming != null) {
            return openWaterSwimming!!
        }
        openWaterSwimming = ImageVector.Builder(
            name = "Connect.OpenWaterSwimming",
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
                    moveTo(89f, 192f)
                    lineToRelative(56f, 44f)
                    lineToRelative(122f, 124f)
                    lineToRelative(106f, -57f)
                    lineToRelative(10f, -25f)
                    lineToRelative(-105f, 41f)
                    lineToRelative(-62f, -93f)
                    lineToRelative(18f, -45f)
                    lineToRelative(-89f, 24f)
                    close()
                    moveTo(346f, 196f)
                    quadToRelative(3f, 17f, -5.5f, 32f)
                    reflectiveQuadToRelative(-24.5f, 20f)
                    quadToRelative(-11f, 4f, -22f, 2f)
                    reflectiveQuadToRelative(-20f, -9f)
                    quadToRelative(-13f, -11f, -16f, -28f)
                    reflectiveQuadToRelative(6f, -31f)
                    close()
                    moveTo(257f, 97f)
                    lineToRelative(124f, 22f)
                    lineToRelative(78f, -19f)
                    lineToRelative(-4f, -23f)
                    lineToRelative(-74f, 20f)
                    lineToRelative(-124f, -24f)
                    lineToRelative(-112f, 24f)
                    lineToRelative(-87f, -20f)
                    lineToRelative(-5f, 23f)
                    lineToRelative(92f, 20f)
                    close()
                    moveTo(381f, 171f)
                    lineToRelative(-124f, -24f)
                    lineToRelative(-112f, 24f)
                    lineToRelative(-102f, -24f)
                    lineToRelative(5f, -22f)
                    lineToRelative(97f, 22f)
                    lineToRelative(112f, -23f)
                    lineToRelative(124f, 23f)
                    lineToRelative(84f, -21f)
                    lineToRelative(4f, 22f)
                    close()
                    moveTo(256f, 47f)
                    lineToRelative(124f, 23f)
                    lineToRelative(69f, -17f)
                    lineToRelative(-4f, -23f)
                    lineToRelative(-65f, 16f)
                    lineToRelative(-124f, -22f)
                    lineToRelative(-112f, 23f)
                    lineToRelative(-76f, -17f)
                    lineToRelative(-5f, 23f)
                    lineToRelative(81f, 18f)
                    close()
                }
            }
        }.build()
        return openWaterSwimming!!
    }

private var openWaterSwimming: ImageVector? = null
