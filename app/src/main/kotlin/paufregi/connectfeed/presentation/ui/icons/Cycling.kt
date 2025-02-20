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

val ConnectIcons.Cycling: ImageVector
    get() {
        if (cycling != null) {
            return cycling!!
        }
        cycling = ImageVector.Builder(
            name = "Connect.Cycling",
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
                    moveTo(267f, 260f)
                    lineToRelative(34f, -33f)
                    horizontalLineToRelative(90f)
                    lineToRelative(-13f, 17f)
                    lineToRelative(-58f, 10f)
                    lineToRelative(-40f, 58f)
                    lineToRelative(-35f, 16f)
                    lineToRelative(-81f, -85f)
                    quadToRelative(-4f, -4f, -3.5f, -9.5f)
                    reflectiveQuadToRelative(4.5f, -8.5f)
                    lineToRelative(80f, -54f)
                    verticalLineToRelative(-101f)
                    horizontalLineToRelative(15f)
                    lineToRelative(24f, 110f)
                    lineToRelative(-49f, 47f)
                    close()
                    moveTo(380f, 193f)
                    quadToRelative(-27f, 0f, -50f, -15f)
                    reflectiveQuadToRelative(-33.5f, -40f)
                    reflectiveQuadToRelative(-5f, -52f)
                    reflectiveQuadToRelative(24.5f, -46f)
                    reflectiveQuadToRelative(46f, -24.5f)
                    reflectiveQuadToRelative(52f, 5f)
                    reflectiveQuadToRelative(40f, 33f)
                    reflectiveQuadToRelative(15f, 49.5f)
                    quadToRelative(0f, 37f, -26.5f, 63.5f)
                    reflectiveQuadToRelative(-62.5f, 26.5f)
                    close()
                    moveTo(380f, 36f)
                    quadToRelative(-21f, 0f, -38f, 11.5f)
                    reflectiveQuadToRelative(-24.5f, 30f)
                    reflectiveQuadToRelative(-3.5f, 39f)
                    reflectiveQuadToRelative(18f, 34.5f)
                    reflectiveQuadToRelative(34.5f, 18f)
                    reflectiveQuadToRelative(39f, -3.5f)
                    reflectiveQuadToRelative(30f, -24.5f)
                    reflectiveQuadToRelative(11.5f, -38f)
                    quadToRelative(0f, -27f, -20f, -47f)
                    reflectiveQuadToRelative(-47f, -20f)
                    close()
                    moveTo(132f, 193f)
                    quadToRelative(-26f, 0f, -49f, -15f)
                    reflectiveQuadToRelative(-33.5f, -40f)
                    reflectiveQuadToRelative(-5f, -52f)
                    reflectiveQuadToRelative(24.5f, -46f)
                    reflectiveQuadToRelative(46f, -24.5f)
                    reflectiveQuadToRelative(52f, 5f)
                    reflectiveQuadToRelative(40f, 33f)
                    reflectiveQuadToRelative(15f, 49.5f)
                    quadToRelative(0f, 37f, -26.5f, 63.5f)
                    reflectiveQuadToRelative(-63.5f, 26.5f)
                    close()
                    moveTo(132f, 36f)
                    quadToRelative(-20f, 0f, -37f, 11.5f)
                    reflectiveQuadToRelative(-24.5f, 30f)
                    reflectiveQuadToRelative(-3.5f, 39f)
                    reflectiveQuadToRelative(18f, 34.5f)
                    reflectiveQuadToRelative(34.5f, 18f)
                    reflectiveQuadToRelative(39f, -3.5f)
                    reflectiveQuadToRelative(30f, -24.5f)
                    reflectiveQuadToRelative(11.5f, -38f)
                    quadToRelative(0f, -27f, -20f, -47f)
                    reflectiveQuadToRelative(-48f, -20f)
                    close()
                    moveTo(312f, 317f)
                    quadToRelative(10f, 0f, 18.5f, 5.5f)
                    reflectiveQuadToRelative(12.5f, 15f)
                    reflectiveQuadToRelative(2f, 19.5f)
                    reflectiveQuadToRelative(-9f, 17f)
                    reflectiveQuadToRelative(-17.5f, 9f)
                    reflectiveQuadToRelative(-19.5f, -1.5f)
                    reflectiveQuadToRelative(-15f, -12.5f)
                    reflectiveQuadToRelative(-6f, -19f)
                    quadToRelative(0f, -13f, 10f, -23f)
                    reflectiveQuadToRelative(24f, -10f)
                    close()
                }
            }
        }.build()
        return cycling!!
    }

private var cycling: ImageVector? = null
