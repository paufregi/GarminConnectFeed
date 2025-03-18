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

val ConnectIcons.Rugby: ImageVector
    get() {
        if (rugby != null) {
            return rugby!!
        }
        rugby = ImageVector.Builder(
            name = "Connect.Rugby",
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
                    moveTo(363f, 300f)
                    quadToRelative(-42f, 52f, -102f, 80f)
                    quadToRelative(-58f, 26f, -122f, 25f)
                    quadToRelative(-5f, 0f, -14f, -1f)
                    quadToRelative(104f, -36f, 180f, -119f)
                    quadToRelative(46f, -51f, 74.5f, -112f)
                    reflectiveQuadToRelative(37.5f, -126f)
                    lineToRelative(2f, 7f)
                    quadToRelative(15f, 64f, 1f, 128f)
                    quadToRelative(-15f, 66f, -57f, 118f)
                    verticalLineToRelative(0f)
                    close()
                    moveTo(100f, 393f)
                    quadToRelative(-23f, -17f, -29f, -45f)
                    lineToRelative(-1f, -3f)
                    quadToRelative(15f, -61f, 45.5f, -117.5f)
                    reflectiveQuadToRelative(73.5f, -104.5f)
                    quadToRelative(72f, -80f, 167f, -123f)
                    quadToRelative(27f, 2f, 46f, 21f)
                    quadToRelative(-5f, 69f, -33.5f, 133.5f)
                    reflectiveQuadToRelative(-77.5f, 117.5f)
                    quadToRelative(-39f, 43f, -88f, 74f)
                    reflectiveQuadToRelative(-103f, 46f)
                    verticalLineToRelative(1f)
                    close()
                    moveTo(176f, 111f)
                    quadToRelative(-76f, 84f, -112f, 190f)
                    quadToRelative(-2f, -53f, 14f, -103.5f)
                    reflectiveQuadToRelative(50.5f, -93.5f)
                    reflectiveQuadToRelative(82.5f, -69.5f)
                    reflectiveQuadToRelative(102f, -32.5f)
                    quadToRelative(-77f, 42f, -137f, 109f)
                    verticalLineToRelative(0f)
                    close()
                }
            }
        }.build()
        return rugby!!
    }

private var rugby: ImageVector? = null
