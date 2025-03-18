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

val ConnectIcons.Yoga: ImageVector
    get() {
        if (yoga != null) {
            return yoga!!
        }
        yoga = ImageVector.Builder(
            name = "Connect.Yoga",
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
                    moveTo(305f, 334f)
                    quadToRelative(0f, -20f, -14.5f, -34.5f)
                    reflectiveQuadToRelative(-35.5f, -14.5f)
                    reflectiveQuadToRelative(-35.5f, 14.5f)
                    reflectiveQuadToRelative(-14.5f, 35f)
                    reflectiveQuadToRelative(14.5f, 35f)
                    reflectiveQuadToRelative(35.5f, 14.5f)
                    reflectiveQuadToRelative(35.5f, -14.5f)
                    reflectiveQuadToRelative(14.5f, -35.5f)
                    close()
                    moveTo(409f, 30f)
                    verticalLineToRelative(0f)
                    quadToRelative(4f, -1f, 6.5f, -9f)
                    reflectiveQuadToRelative(-2.5f, -14.5f)
                    reflectiveQuadToRelative(-20f, -6.5f)
                    horizontalLineToRelative(-212f)
                    lineToRelative(25f, 24f)
                    lineToRelative(93f, 13f)
                    lineToRelative(-31f, 24f)
                    lineToRelative(-87f, -15f)
                    lineToRelative(-45f, -46f)
                    horizontalLineToRelative(-26f)
                    quadToRelative(-10f, 0f, -14f, 5.5f)
                    reflectiveQuadToRelative(-1.5f, 12.5f)
                    reflectiveQuadToRelative(8f, 11f)
                    reflectiveQuadToRelative(87.5f, 66f)
                    lineToRelative(7f, 6f)
                    quadToRelative(8f, 9f, 8f, 16f)
                    verticalLineToRelative(48f)
                    quadToRelative(0f, 12f, -10f, 0f)
                    quadToRelative(-1f, -2f, -11f, -13f)
                    quadToRelative(-13f, -14f, -26.5f, -27.5f)
                    reflectiveQuadToRelative(-37.5f, -30.5f)
                    lineToRelative(-21f, -14f)
                    lineToRelative(-14f, 13f)
                    lineToRelative(9f, 8f)
                    quadToRelative(11f, 11f, 22f, 24f)
                    quadToRelative(16f, 18f, 28f, 37f)
                    quadToRelative(21f, 34f, 46f, 70f)
                    lineToRelative(20f, 28f)
                    horizontalLineToRelative(91f)
                    lineToRelative(20f, -29f)
                    quadToRelative(24f, -35f, 46f, -69f)
                    quadToRelative(17f, -28f, 40f, -51f)
                    quadToRelative(12f, -12f, 20f, -18f)
                    verticalLineToRelative(0f)
                    lineToRelative(-14f, -13f)
                    lineToRelative(-21f, 15f)
                    quadToRelative(-24f, 17f, -37f, 28f)
                    quadToRelative(-29f, 26f, -40f, 42f)
                    quadToRelative(-4f, 6f, -7f, 6.5f)
                    reflectiveQuadToRelative(-3f, -5.5f)
                    verticalLineToRelative(-50f)
                    quadToRelative(0f, -7f, 8f, -15f)
                    lineToRelative(9f, -7f)
                    close()
                }
            }
        }.build()
        return yoga!!
    }

private var yoga: ImageVector? = null
