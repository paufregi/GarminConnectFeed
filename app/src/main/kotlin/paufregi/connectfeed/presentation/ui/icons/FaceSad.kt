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

val ConnectIcons.FaceSad: ImageVector
    get() {
        if (faceSad != null) {
            return faceSad!!
        }
        faceSad = ImageVector.Builder(
            name = "Connect.FaceSad",
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
                    quadToRelative(39f, 16f, 81.5f, 16.5f)
                    reflectiveQuadToRelative(82f, -15.5f)
                    reflectiveQuadToRelative(69.5f, -46f)
                    reflectiveQuadToRelative(46.5f, -69f)
                    reflectiveQuadToRelative(17f, -81.5f)
                    reflectiveQuadToRelative(-15.5f, -81.5f)
                    reflectiveQuadToRelative(-46f, -69.5f)
                    reflectiveQuadToRelative(-69f, -47f)
                    reflectiveQuadToRelative(-83f, -16.5f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(254f, 89f)
                    quadToRelative(42f, 0f, 74f, -27f)
                    quadToRelative(3f, -3f, 7f, -2.5f)
                    reflectiveQuadToRelative(7f, 3.5f)
                    reflectiveQuadToRelative(2.5f, 7f)
                    reflectiveQuadToRelative(-3.5f, 7f)
                    quadToRelative(-37f, 32f, -85.5f, 32.5f)
                    reflectiveQuadToRelative(-85.5f, -31.5f)
                    quadToRelative(-2f, -1f, -3f, -3f)
                    reflectiveQuadToRelative(-1f, -4f)
                    reflectiveQuadToRelative(0.5f, -3.5f)
                    reflectiveQuadToRelative(2f, -3f)
                    reflectiveQuadToRelative(3f, -2.5f)
                    reflectiveQuadToRelative(3.5f, -1.5f)
                    reflectiveQuadToRelative(4f, 0.5f)
                    lineToRelative(4f, 2f)
                    quadToRelative(31f, 26f, 71f, 26f)
                    verticalLineToRelative(0f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(336f, 213f)
                    quadToRelative(-11f, 0f, -19f, -5.5f)
                    reflectiveQuadToRelative(-12f, -15f)
                    reflectiveQuadToRelative(-2f, -19.5f)
                    reflectiveQuadToRelative(9f, -17f)
                    reflectiveQuadToRelative(17f, -9f)
                    reflectiveQuadToRelative(19.5f, 2f)
                    reflectiveQuadToRelative(15f, 12f)
                    reflectiveQuadToRelative(5.5f, 19f)
                    quadToRelative(0f, 13f, -10f, 23f)
                    reflectiveQuadToRelative(-23f, 10f)
                    close()
                    moveTo(177f, 213f)
                    quadToRelative(-10f, 0f, -18.5f, -5.5f)
                    reflectiveQuadToRelative(-12.5f, -15f)
                    reflectiveQuadToRelative(-2f, -19.5f)
                    reflectiveQuadToRelative(9f, -17f)
                    reflectiveQuadToRelative(17f, -9f)
                    reflectiveQuadToRelative(19.5f, 2f)
                    reflectiveQuadToRelative(15f, 12f)
                    reflectiveQuadToRelative(5.5f, 19f)
                    quadToRelative(0f, 13f, -9.5f, 23f)
                    reflectiveQuadToRelative(-23.5f, 10f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(315f, 282f)
                    quadToRelative(-3f, 0f, -5f, -1f)
                    reflectiveQuadToRelative(-3.5f, -3.5f)
                    reflectiveQuadToRelative(-1.5f, -5f)
                    reflectiveQuadToRelative(1f, -4.5f)
                    quadToRelative(13f, -26f, 47f, -33f)
                    quadToRelative(14f, -3f, 28f, -3f)
                    quadToRelative(8f, 0f, 16f, 1f)
                    horizontalLineToRelative(6f)
                    quadToRelative(4f, 1f, 6.5f, 4f)
                    reflectiveQuadToRelative(2.5f, 7f)
                    reflectiveQuadToRelative(-3f, 6.5f)
                    reflectiveQuadToRelative(-7f, 2.5f)
                    horizontalLineToRelative(-7f)
                    quadToRelative(-7f, -1f, -14f, -1f)
                    quadToRelative(-12f, 0f, -23f, 2f)
                    quadToRelative(-26f, 6f, -34f, 23f)
                    quadToRelative(-1f, 2f, -3.5f, 3.5f)
                    reflectiveQuadToRelative(-5.5f, 1.5f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(111f, 253f)
                    quadToRelative(-4f, 0f, -7f, -2.5f)
                    reflectiveQuadToRelative(-3.5f, -6.5f)
                    reflectiveQuadToRelative(2f, -7f)
                    reflectiveQuadToRelative(6.5f, -4f)
                    horizontalLineToRelative(6f)
                    quadToRelative(8f, -1f, 16f, -1f)
                    quadToRelative(14f, 0f, 28f, 3f)
                    quadToRelative(35f, 7f, 47f, 33f)
                    quadToRelative(2f, 4f, 1f, 7.5f)
                    reflectiveQuadToRelative(-4f, 5f)
                    reflectiveQuadToRelative(-5f, 2f)
                    reflectiveQuadToRelative(-4f, -0.5f)
                    reflectiveQuadToRelative(-3.5f, -2f)
                    reflectiveQuadToRelative(-2.5f, -3f)
                    quadToRelative(-4f, -9f, -13f, -14.5f)
                    reflectiveQuadToRelative(-20.5f, -8f)
                    reflectiveQuadToRelative(-23.5f, -2.5f)
                    horizontalLineToRelative(-9f)
                    close()
                }
            }
        }.build()
        return faceSad!!
    }

private var faceSad: ImageVector? = null
