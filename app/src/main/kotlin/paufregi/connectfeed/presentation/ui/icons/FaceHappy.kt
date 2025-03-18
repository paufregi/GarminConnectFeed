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

val ConnectIcons.FaceHappy: ImageVector
    get() {
        if (faceHappy != null) {
            return faceHappy!!
        }
        faceHappy = ImageVector.Builder(
            name = "Connect.FaceHappy",
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
                    moveTo(256f, -21f)
                    quadToRelative(-37f, 0f, -72f, 12.5f)
                    reflectiveQuadToRelative(-63.5f, 35.5f)
                    reflectiveQuadToRelative(-47.5f, 54.5f)
                    reflectiveQuadToRelative(-26f, 68f)
                    reflectiveQuadToRelative(-2f, 73f)
                    reflectiveQuadToRelative(22.5f, 69f)
                    reflectiveQuadToRelative(44.5f, 57.5f)
                    reflectiveQuadToRelative(61f, 40f)
                    quadToRelative(39f, 16f, 81.5f, 16.5f)
                    reflectiveQuadToRelative(82f, -15.5f)
                    reflectiveQuadToRelative(69.5f, -46f)
                    reflectiveQuadToRelative(46.5f, -69f)
                    reflectiveQuadToRelative(17f, -81.5f)
                    reflectiveQuadToRelative(-15.5f, -82f)
                    reflectiveQuadToRelative(-46f, -69.5f)
                    reflectiveQuadToRelative(-69.5f, -46.5f)
                    reflectiveQuadToRelative(-82.5f, -16.5f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(176f, 110f)
                    quadToRelative(-3f, 0f, -5.5f, -1.5f)
                    reflectiveQuadToRelative(-4f, -4.5f)
                    reflectiveQuadToRelative(-0.5f, -6f)
                    reflectiveQuadToRelative(3f, -5f)
                    quadToRelative(37f, -32f, 85.5f, -32.5f)
                    reflectiveQuadToRelative(85.5f, 30.5f)
                    quadToRelative(4f, 3f, 4f, 7f)
                    reflectiveQuadToRelative(-2.5f, 7f)
                    reflectiveQuadToRelative(-6.5f, 3.5f)
                    reflectiveQuadToRelative(-7f, -2.5f)
                    quadToRelative(-32f, -26f, -73f, -25.5f)
                    reflectiveQuadToRelative(-73f, 27.5f)
                    quadToRelative(-2f, 2f, -6f, 2f)
                    close()
                    moveTo(335f, 253f)
                    quadToRelative(-10f, 0f, -18.5f, -5.5f)
                    reflectiveQuadToRelative(-12.5f, -15f)
                    reflectiveQuadToRelative(-2f, -19.5f)
                    reflectiveQuadToRelative(9.5f, -17f)
                    reflectiveQuadToRelative(17.5f, -9f)
                    reflectiveQuadToRelative(19f, 2f)
                    reflectiveQuadToRelative(14.5f, 12f)
                    reflectiveQuadToRelative(5.5f, 18f)
                    quadToRelative(0f, 14f, -9.5f, 24f)
                    reflectiveQuadToRelative(-23.5f, 10f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(177f, 253f)
                    quadToRelative(-10f, 0f, -18.5f, -5.5f)
                    reflectiveQuadToRelative(-12.5f, -15f)
                    reflectiveQuadToRelative(-2f, -19.5f)
                    reflectiveQuadToRelative(9f, -17f)
                    reflectiveQuadToRelative(17f, -9f)
                    reflectiveQuadToRelative(19.5f, 2f)
                    reflectiveQuadToRelative(15f, 12f)
                    reflectiveQuadToRelative(5.5f, 18f)
                    quadToRelative(0f, 14f, -9.5f, 24f)
                    reflectiveQuadToRelative(-23.5f, 10f)
                    verticalLineToRelative(0f)
                    close()
                }
            }
        }.build()
        return faceHappy!!
    }

private var faceHappy: ImageVector? = null
