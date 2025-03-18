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

val ConnectIcons.FaceVerySad: ImageVector
    get() {
        if (faceVerySad != null) {
            return faceVerySad!!
        }
        faceVerySad = ImageVector.Builder(
            name = "Connect.FaceVerySad",
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
                    verticalLineToRelative(0f)
                    close()
                    moveTo(254f, 100f)
                    quadToRelative(-45f, 0f, -79f, -47f)
                    lineToRelative(160f, -2f)
                    quadToRelative(-17f, 24f, -37.5f, 36.5f)
                    reflectiveQuadToRelative(-43.5f, 12.5f)
                    close()
                    moveTo(305f, 196f)
                    quadToRelative(-4f, 0f, -7f, -3f)
                    reflectiveQuadToRelative(-3f, -7f)
                    quadToRelative(0f, -18f, 13f, -30.5f)
                    reflectiveQuadToRelative(31f, -12.5f)
                    reflectiveQuadToRelative(30.5f, 12.5f)
                    reflectiveQuadToRelative(12.5f, 30.5f)
                    quadToRelative(0f, 4f, -2.5f, 7f)
                    reflectiveQuadToRelative(-6.5f, 3.5f)
                    reflectiveQuadToRelative(-7.5f, -2f)
                    reflectiveQuadToRelative(-3.5f, -6.5f)
                    verticalLineToRelative(-2f)
                    quadToRelative(0f, -4f, -2f, -8.5f)
                    reflectiveQuadToRelative(-5f, -7.5f)
                    reflectiveQuadToRelative(-7.5f, -5f)
                    reflectiveQuadToRelative(-9f, -2f)
                    reflectiveQuadToRelative(-9f, 2f)
                    reflectiveQuadToRelative(-7.5f, 5f)
                    reflectiveQuadToRelative(-5f, 7.5f)
                    reflectiveQuadToRelative(-2f, 8.5f)
                    reflectiveQuadToRelative(-3f, 7f)
                    reflectiveQuadToRelative(-7f, 3f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(145f, 196f)
                    quadToRelative(-4f, 0f, -7f, -3f)
                    reflectiveQuadToRelative(-3f, -7f)
                    quadToRelative(0f, -18f, 13f, -30.5f)
                    reflectiveQuadToRelative(31f, -12.5f)
                    reflectiveQuadToRelative(30.5f, 12.5f)
                    reflectiveQuadToRelative(12.5f, 30.5f)
                    quadToRelative(0f, 4f, -2.5f, 7f)
                    reflectiveQuadToRelative(-6.5f, 3.5f)
                    reflectiveQuadToRelative(-7f, -2f)
                    reflectiveQuadToRelative(-4f, -6.5f)
                    verticalLineToRelative(-2f)
                    quadToRelative(0f, -9f, -7f, -16f)
                    reflectiveQuadToRelative(-16.5f, -7f)
                    reflectiveQuadToRelative(-16.5f, 7f)
                    reflectiveQuadToRelative(-7f, 16f)
                    quadToRelative(0f, 4f, -3f, 7f)
                    reflectiveQuadToRelative(-7f, 3f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(315f, 282f)
                    quadToRelative(-3f, 0f, -5f, -1f)
                    reflectiveQuadToRelative(-3.5f, -3f)
                    reflectiveQuadToRelative(-1.5f, -5f)
                    reflectiveQuadToRelative(1f, -5f)
                    quadToRelative(13f, -26f, 47f, -33f)
                    quadToRelative(14f, -3f, 29f, -3f)
                    quadToRelative(7f, 0f, 15f, 1f)
                    lineToRelative(6f, 1f)
                    quadToRelative(5f, 0f, 7f, 3.5f)
                    reflectiveQuadToRelative(1.5f, 7.5f)
                    reflectiveQuadToRelative(-4f, 6.5f)
                    reflectiveQuadToRelative(-7.5f, 1.5f)
                    horizontalLineToRelative(-5f)
                    quadToRelative(-7f, -1f, -13f, -1f)
                    quadToRelative(-12f, 0f, -24f, 2f)
                    quadToRelative(-25f, 6f, -34f, 23f)
                    quadToRelative(-1f, 3f, -3.5f, 4f)
                    reflectiveQuadToRelative(-5.5f, 1f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(111f, 253f)
                    quadToRelative(-4f, 1f, -7f, -2f)
                    reflectiveQuadToRelative(-3.5f, -7f)
                    reflectiveQuadToRelative(2f, -7f)
                    reflectiveQuadToRelative(6.5f, -3f)
                    lineToRelative(6f, -1f)
                    quadToRelative(8f, -1f, 16f, -1f)
                    quadToRelative(14f, 0f, 28f, 3f)
                    quadToRelative(35f, 7f, 47f, 33f)
                    quadToRelative(2f, 4f, 1f, 8f)
                    reflectiveQuadToRelative(-4f, 5f)
                    reflectiveQuadToRelative(-5f, 1.5f)
                    reflectiveQuadToRelative(-4f, -0.5f)
                    reflectiveQuadToRelative(-3.5f, -2f)
                    reflectiveQuadToRelative(-2.5f, -3f)
                    quadToRelative(-8f, -17f, -33f, -23f)
                    quadToRelative(-12f, -2f, -24f, -2f)
                    horizontalLineToRelative(-9f)
                    close()
                }
            }
        }.build()
        return faceVerySad!!
    }

private var faceVerySad: ImageVector? = null
