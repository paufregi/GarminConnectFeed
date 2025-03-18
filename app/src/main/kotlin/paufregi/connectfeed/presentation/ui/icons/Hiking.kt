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

val ConnectIcons.Hiking: ImageVector
    get() {
        if (hiking != null) {
            return hiking!!
        }
        hiking = ImageVector.Builder(
            name = "Connect.Hiking",
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
                    moveTo(306f, 333f)
                    quadToRelative(11f, 0f, 20f, 6f)
                    reflectiveQuadToRelative(13f, 16f)
                    reflectiveQuadToRelative(2f, 21f)
                    reflectiveQuadToRelative(-9.5f, 18.5f)
                    reflectiveQuadToRelative(-18.5f, 10f)
                    reflectiveQuadToRelative(-21f, -2f)
                    reflectiveQuadToRelative(-16f, -13.5f)
                    reflectiveQuadToRelative(-6f, -20f)
                    quadToRelative(0f, -15f, 10.5f, -25.5f)
                    reflectiveQuadToRelative(25.5f, -10.5f)
                    close()
                    moveTo(180f, 93f)
                    lineToRelative(-46f, -103f)
                    lineToRelative(13f, -11f)
                    lineToRelative(73f, 104f)
                    lineToRelative(4f, 18f)
                    lineToRelative(-37f, 43f)
                    close()
                    moveTo(168f, 212f)
                    lineToRelative(56f, 128f)
                    lineToRelative(-37f, 12f)
                    lineToRelative(-28f, -34f)
                    quadToRelative(-29f, -37f, -33f, -52f)
                    quadToRelative(-5f, -17f, -3f, -24f)
                    reflectiveQuadToRelative(13f, -13f)
                    close()
                    moveTo(390f, 250f)
                    horizontalLineToRelative(-24f)
                    lineToRelative(-10f, -21f)
                    lineToRelative(-27f, 17f)
                    lineToRelative(-28f, 65f)
                    lineToRelative(-55f, 21f)
                    lineToRelative(-57f, -132f)
                    quadToRelative(-3f, -6f, -1.5f, -13f)
                    reflectiveQuadToRelative(6.5f, -13f)
                    lineToRelative(70f, -83f)
                    lineToRelative(22f, 43f)
                    lineToRelative(-25f, 47f)
                    lineToRelative(30f, 68f)
                    lineToRelative(16f, -33f)
                    lineToRelative(39f, -9f)
                    lineToRelative(-110f, -228f)
                    horizontalLineToRelative(24f)
                    lineToRelative(22f, 47f)
                    lineToRelative(14f, -47f)
                    lineToRelative(19f, 5f)
                    lineToRelative(-9f, 90f)
                    close()
                }
            }
        }.build()
        return hiking!!
    }

private var hiking: ImageVector? = null
