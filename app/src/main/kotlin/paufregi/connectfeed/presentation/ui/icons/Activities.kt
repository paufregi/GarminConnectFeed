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

val ConnectIcons.Activities: ImageVector
    get() {
        if (activities != null) {
            return activities!!
        }
        activities = ImageVector.Builder(
            name = "Connect.Activities",
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
                    moveTo(427f, 387f)
                    lineToRelative(-96f, -107f)
                    quadToRelative(-5f, -6f, -8f, -12f)
                    lineToRelative(-6f, -14f)
                    quadToRelative(-4f, -8f, -5.5f, -13.5f)
                    reflectiveQuadToRelative(-0.5f, -17.5f)
                    quadToRelative(0f, -26f, 19f, -133f)
                    lineToRelative(19f, -102f)
                    lineToRelative(-14f, -9f)
                    lineToRelative(-13f, 9f)
                    lineToRelative(-52f, 156f)
                    lineToRelative(-3f, 6f)
                    quadToRelative(-4f, 6f, -11f, 6f)
                    reflectiveQuadToRelative(-11f, -6f)
                    quadToRelative(-2f, -3f, -3f, -6f)
                    lineToRelative(-52f, -156f)
                    lineToRelative(-15f, -9f)
                    lineToRelative(-12f, 9f)
                    lineToRelative(19f, 101f)
                    quadToRelative(19f, 106f, 19f, 134f)
                    quadToRelative(2f, 30f, -20f, 59f)
                    lineToRelative(-96f, 104f)
                    lineToRelative(14f, 12f)
                    lineToRelative(126f, -94f)
                    horizontalLineToRelative(64f)
                    lineToRelative(124f, 95f)
                    close()
                    moveTo(297f, 365f)
                    quadToRelative(0f, 17f, -12f, 28.5f)
                    reflectiveQuadToRelative(-29f, 11.5f)
                    quadToRelative(-19f, 0f, -31.5f, -14.5f)
                    reflectiveQuadToRelative(-9.5f, -33.5f)
                    quadToRelative(3f, -11f, 12f, -20f)
                    reflectiveQuadToRelative(21f, -11f)
                    quadToRelative(18f, -4f, 33.5f, 8f)
                    reflectiveQuadToRelative(15.5f, 31f)
                    close()
                }
            }
        }.build()
        return activities!!
    }

private var activities: ImageVector? = null
