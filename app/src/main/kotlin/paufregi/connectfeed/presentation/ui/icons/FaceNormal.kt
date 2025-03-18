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

val ConnectIcons.FaceNormal: ImageVector
    get() {
        if (faceNormal != null) {
            return faceNormal!!
        }
        faceNormal = ImageVector.Builder(
            name = "Connect.FaceNormal",
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
                    quadToRelative(-37f, 0f, -71.5f, 12.5f)
                    reflectiveQuadToRelative(-63.5f, 35.5f)
                    reflectiveQuadToRelative(-48f, 55f)
                    reflectiveQuadToRelative(-26f, 68f)
                    reflectiveQuadToRelative(-2f, 72.5f)
                    reflectiveQuadToRelative(22.5f, 69f)
                    reflectiveQuadToRelative(44.5f, 57.5f)
                    reflectiveQuadToRelative(61f, 40f)
                    quadToRelative(39f, 16f, 81.5f, 16f)
                    reflectiveQuadToRelative(82f, -16f)
                    reflectiveQuadToRelative(69.5f, -45.5f)
                    reflectiveQuadToRelative(46.5f, -68.5f)
                    reflectiveQuadToRelative(17f, -81.5f)
                    reflectiveQuadToRelative(-15.5f, -81.5f)
                    reflectiveQuadToRelative(-45.5f, -69.5f)
                    reflectiveQuadToRelative(-69f, -47f)
                    reflectiveQuadToRelative(-83.5f, -16.5f)
                    close()
                    moveTo(205f, 100f)
                    quadToRelative(-5f, 0f, -8f, -2.5f)
                    reflectiveQuadToRelative(-3f, -7f)
                    reflectiveQuadToRelative(3f, -7.5f)
                    reflectiveQuadToRelative(8f, -3f)
                    horizontalLineToRelative(103f)
                    quadToRelative(4f, 0f, 7f, 3f)
                    reflectiveQuadToRelative(3f, 7.5f)
                    reflectiveQuadToRelative(-3f, 7f)
                    reflectiveQuadToRelative(-7f, 2.5f)
                    horizontalLineToRelative(-103f)
                    close()
                    moveTo(336f, 253f)
                    quadToRelative(-10f, 0f, -18.5f, -5.5f)
                    reflectiveQuadToRelative(-12.5f, -15f)
                    reflectiveQuadToRelative(-2f, -19.5f)
                    reflectiveQuadToRelative(9f, -17f)
                    reflectiveQuadToRelative(17f, -9f)
                    reflectiveQuadToRelative(19.5f, 2f)
                    reflectiveQuadToRelative(15f, 12.5f)
                    reflectiveQuadToRelative(5.5f, 18.5f)
                    quadToRelative(0f, 13f, -9.5f, 23f)
                    reflectiveQuadToRelative(-23.5f, 10f)
                    close()
                    moveTo(177f, 253f)
                    quadToRelative(-10f, 0f, -18.5f, -5.5f)
                    reflectiveQuadToRelative(-12.5f, -15f)
                    reflectiveQuadToRelative(-2f, -19.5f)
                    reflectiveQuadToRelative(9.5f, -17f)
                    reflectiveQuadToRelative(17f, -9f)
                    reflectiveQuadToRelative(19f, 2f)
                    reflectiveQuadToRelative(15f, 12.5f)
                    reflectiveQuadToRelative(5.5f, 18.5f)
                    quadToRelative(0f, 13f, -9.5f, 23f)
                    reflectiveQuadToRelative(-23.5f, 10f)
                    close()
                }
            }
        }.build()
        return faceNormal!!
    }

private var faceNormal: ImageVector? = null
