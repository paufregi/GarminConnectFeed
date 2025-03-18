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

val ConnectIcons.TreadmillRunning: ImageVector
    get() {
        if (treadmillRunning != null) {
            return treadmillRunning!!
        }
        treadmillRunning = ImageVector.Builder(
            name = "Connect.TreadmillRunning",
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
                    moveTo(436f, 332f)
                    lineToRelative(-34f, -280f)
                    lineToRelative(-164f, -16f)
                    lineToRelative(74f, 105f)
                    lineToRelative(-53f, 69f)
                    lineToRelative(31f, 40f)
                    lineToRelative(49f, -29f)
                    lineToRelative(63f, 55f)
                    lineToRelative(-11f, 11f)
                    lineToRelative(-52f, -27f)
                    lineToRelative(-72f, 61f)
                    lineToRelative(-67f, 11f)
                    lineToRelative(-56f, -56f)
                    lineToRelative(11f, -11f)
                    lineToRelative(47f, 36f)
                    lineToRelative(38f, -10f)
                    lineToRelative(-24f, -31f)
                    quadToRelative(-25f, -32f, -30f, -40f)
                    reflectiveQuadToRelative(-2f, -14f)
                    quadToRelative(1f, -3f, 5f, -5f)
                    lineToRelative(71f, -58f)
                    lineToRelative(-38f, -103f)
                    lineToRelative(6f, -5f)
                    lineToRelative(-185f, -17f)
                    verticalLineToRelative(-22f)
                    horizontalLineToRelative(393f)
                    verticalLineToRelative(56f)
                    lineToRelative(33f, 280f)
                    horizontalLineToRelative(-33f)
                    close()
                    moveTo(212f, 153f)
                    lineToRelative(-39f, 30f)
                    lineToRelative(-19f, -49f)
                    lineToRelative(-89f, -60f)
                    lineToRelative(11f, -11f)
                    lineToRelative(107f, 52f)
                    close()
                    moveTo(351f, 317f)
                    quadToRelative(10f, 6f, 14.5f, 17f)
                    reflectiveQuadToRelative(2.5f, 23f)
                    quadToRelative(-4f, 16f, -17.5f, 24.5f)
                    reflectiveQuadToRelative(-29.5f, 6f)
                    reflectiveQuadToRelative(-25f, -16.5f)
                    quadToRelative(-7f, -12f, -6f, -26f)
                    reflectiveQuadToRelative(11f, -23.5f)
                    reflectiveQuadToRelative(24f, -11f)
                    reflectiveQuadToRelative(26f, 6.5f)
                    close()
                }
            }
        }.build()
        return treadmillRunning!!
    }

private var treadmillRunning: ImageVector? = null
