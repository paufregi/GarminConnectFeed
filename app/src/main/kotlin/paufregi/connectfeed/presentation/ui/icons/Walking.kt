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

val ConnectIcons.Walking: ImageVector
    get() {
        if (walking != null) {
            return walking!!
        }
        walking = ImageVector.Builder(
            name = "Connect.Walking",
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
                    moveTo(247f, 339f)
                    quadToRelative(-9f, 10f, -9.5f, 24f)
                    reflectiveQuadToRelative(7f, 25f)
                    reflectiveQuadToRelative(21f, 15.5f)
                    reflectiveQuadToRelative(26.5f, -0.5f)
                    reflectiveQuadToRelative(20f, -17f)
                    reflectiveQuadToRelative(5.5f, -26f)
                    reflectiveQuadToRelative(-11.5f, -23f)
                    quadToRelative(-12f, -13f, -30f, -12f)
                    reflectiveQuadToRelative(-29f, 14f)
                    close()
                    moveTo(200f, 139f)
                    lineToRelative(47f, -42f)
                    lineToRelative(-29f, -39f)
                    lineToRelative(-84f, -68f)
                    lineToRelative(-21f, 4f)
                    lineToRelative(70f, 80f)
                    close()
                    moveTo(339f, 225f)
                    lineToRelative(-38f, 73f)
                    lineToRelative(-63f, 15f)
                    lineToRelative(-68f, -50f)
                    lineToRelative(-30f, -85f)
                    lineToRelative(15f, -6f)
                    lineToRelative(40f, 62f)
                    lineToRelative(25f, 20f)
                    quadToRelative(-3f, -37f, -8f, -61f)
                    quadToRelative(-2f, -13f, -2f, -19f)
                    reflectiveQuadToRelative(5f, -10f)
                    lineToRelative(84f, -78f)
                    lineToRelative(53f, -95f)
                    lineToRelative(24f, -12f)
                    lineToRelative(-54f, 140f)
                    lineToRelative(-40f, 57f)
                    lineToRelative(4f, 46f)
                    lineToRelative(38f, -26f)
                    lineToRelative(63f, -21f)
                    lineToRelative(12f, 11f)
                    close()
                }
            }
        }.build()
        return walking!!
    }

private var walking: ImageVector? = null
