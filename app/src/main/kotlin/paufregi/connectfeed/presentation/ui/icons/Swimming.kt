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

val ConnectIcons.Swimming: ImageVector
    get() {
        if (swimming != null) {
            return swimming!!
        }
        swimming = ImageVector.Builder(
            name = "Connect.Swimming",
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
                    moveTo(145f, 211f)
                    lineToRelative(-56f, -44f)
                    lineToRelative(56f, 14f)
                    lineToRelative(89f, -25f)
                    lineToRelative(-18f, 45f)
                    lineToRelative(62f, 93f)
                    lineToRelative(105f, -40f)
                    lineToRelative(-10f, 24f)
                    lineToRelative(-106f, 58f)
                    close()
                    moveTo(346f, 172f)
                    lineToRelative(-82f, -15f)
                    quadToRelative(-6f, 10f, -6.5f, 21f)
                    reflectiveQuadToRelative(3.5f, 21f)
                    reflectiveQuadToRelative(13f, 17f)
                    reflectiveQuadToRelative(20f, 9f)
                    reflectiveQuadToRelative(22f, -1f)
                    quadToRelative(16f, -6f, 24.5f, -21f)
                    reflectiveQuadToRelative(5.5f, -31f)
                    close()
                    moveTo(381f, 94f)
                    lineToRelative(-124f, -22f)
                    lineToRelative(-112f, 23f)
                    lineToRelative(-92f, -20f)
                    lineToRelative(5f, -22f)
                    lineToRelative(87f, 19f)
                    lineToRelative(112f, -24f)
                    lineToRelative(124f, 24f)
                    lineToRelative(74f, -19f)
                    lineToRelative(4f, 22f)
                    close()
                    moveTo(257f, 122f)
                    lineToRelative(124f, 24f)
                    lineToRelative(88f, -22f)
                    lineToRelative(-4f, -23f)
                    lineToRelative(-84f, 21f)
                    lineToRelative(-124f, -23f)
                    lineToRelative(-112f, 23f)
                    lineToRelative(-97f, -22f)
                    lineToRelative(-5f, 22f)
                    lineToRelative(102f, 24f)
                    close()
                }
            }
        }.build()
        return swimming!!
    }

private var swimming: ImageVector? = null
