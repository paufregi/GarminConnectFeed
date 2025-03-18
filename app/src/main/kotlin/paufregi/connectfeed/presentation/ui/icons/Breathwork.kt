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

val ConnectIcons.Breathwork: ImageVector
    get() {
        if (breathwork != null) {
            return breathwork!!
        }
        breathwork = ImageVector.Builder(
            name = "Connect.Breathwork",
            defaultWidth = 522.dp,
            defaultHeight = 512.dp,
            viewportWidth = 522f,
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
                    moveTo(351f, 405f)
                    quadToRelative(22f, 3f, 38.5f, -10.5f)
                    reflectiveQuadToRelative(19f, -34.5f)
                    reflectiveQuadToRelative(-10.5f, -38f)
                    reflectiveQuadToRelative(-34f, -19f)
                    lineToRelative(-111f, -14f)
                    lineToRelative(-3f, 23f)
                    lineToRelative(111f, 13f)
                    quadToRelative(12f, 2f, 19f, 11f)
                    reflectiveQuadToRelative(6f, 21f)
                    reflectiveQuadToRelative(-10.5f, 19.5f)
                    reflectiveQuadToRelative(-22f, 6f)
                    reflectiveQuadToRelative(-20f, -13.5f)
                    reflectiveQuadToRelative(-3.5f, -24f)
                    lineToRelative(-22f, -3f)
                    quadToRelative(-6f, 22f, 7.5f, 41f)
                    reflectiveQuadToRelative(35.5f, 22f)
                    close()
                    moveTo(198f, 248f)
                    quadToRelative(16f, 10f, 21f, 28f)
                    reflectiveQuadToRelative(-4.5f, 34.5f)
                    reflectiveQuadToRelative(-27.5f, 21.5f)
                    reflectiveQuadToRelative(-34.5f, -4.5f)
                    reflectiveQuadToRelative(-21f, -27.5f)
                    reflectiveQuadToRelative(4.5f, -34.5f)
                    reflectiveQuadToRelative(27.5f, -21.5f)
                    reflectiveQuadToRelative(34.5f, 4f)
                    close()
                    moveTo(301f, -21f)
                    lineToRelative(23f, 34f)
                    lineToRelative(-69f, -22f)
                    close()
                    moveTo(123f, 227f)
                    lineToRelative(78f, -18f)
                    lineToRelative(42f, -81f)
                    lineToRelative(93f, -53f)
                    lineToRelative(-1f, -20f)
                    lineToRelative(-15f, 5f)
                    lineToRelative(4f, -24f)
                    lineToRelative(-14f, -5f)
                    quadToRelative(-15f, -5f, -23f, -7f)
                    lineToRelative(-8f, -2f)
                    quadToRelative(-103f, -28f, -132f, -30f)
                    quadToRelative(-24f, -3f, -31.5f, 2.5f)
                    reflectiveQuadToRelative(-8.5f, 24.5f)
                    quadToRelative(-1f, 16f, 1f, 92f)
                    lineToRelative(1f, 73f)
                    close()
                    moveTo(193f, 137f)
                    lineToRelative(-7f, -79f)
                    lineToRelative(117f, 7f)
                    lineToRelative(-82f, 29f)
                    close()
                    moveTo(332f, 276f)
                    verticalLineToRelative(0f)
                    lineToRelative(-99f, -12f)
                    lineToRelative(3f, -23f)
                    lineToRelative(99f, 12f)
                    quadToRelative(7f, 1f, 12.5f, -3.5f)
                    reflectiveQuadToRelative(6.5f, -11.5f)
                    reflectiveQuadToRelative(-3.5f, -12.5f)
                    reflectiveQuadToRelative(-11.5f, -6.5f)
                    quadToRelative(-10f, -1f, -16f, 8f)
                    lineToRelative(-14f, -18f)
                    quadToRelative(14f, -15f, 33f, -12f)
                    quadToRelative(17f, 2f, 27f, 15f)
                    reflectiveQuadToRelative(8f, 29.5f)
                    reflectiveQuadToRelative(-15f, 26.5f)
                    reflectiveQuadToRelative(-30f, 8f)
                    close()
                }
            }
        }.build()
        return breathwork!!
    }

private var breathwork: ImageVector? = null
