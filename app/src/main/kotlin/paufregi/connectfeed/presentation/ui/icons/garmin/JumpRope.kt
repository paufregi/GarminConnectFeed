package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.JumpRope: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.JumpRope",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(266f, 128f)
            quadToRelative(18f, 0f, 30.5f, -12.5f)
            reflectiveQuadToRelative(12.5f, -30f)
            reflectiveQuadToRelative(-12.5f, -30f)
            reflectiveQuadToRelative(-30.5f, -12.5f)
            reflectiveQuadToRelative(-30.5f, 12.5f)
            reflectiveQuadToRelative(-12.5f, 30f)
            reflectiveQuadToRelative(12.5f, 30f)
            reflectiveQuadToRelative(30.5f, 12.5f)
            close()
            moveTo(411f, 256f)
            lineToRelative(4f, -7f)
            lineToRelative(-61f, -39f)
            lineToRelative(-51f, -61f)
            horizontalLineToRelative(-74f)
            lineToRelative(-51f, 61f)
            lineToRelative(-61f, 39f)
            lineToRelative(6f, 7f)
            horizontalLineToRelative(16f)
            lineToRelative(52f, -21f)
            lineToRelative(32f, -31f)
            lineToRelative(-21f, 255f)
            lineToRelative(11f, 10f)
            lineToRelative(10f, -10f)
            lineToRelative(6f, -29f)
            quadToRelative(22f, -106f, 34f, -124f)
            lineToRelative(3f, -2f)
            lineToRelative(4f, 2f)
            quadToRelative(12f, 18f, 33f, 124f)
            lineToRelative(6f, 29f)
            lineToRelative(10f, 10f)
            lineToRelative(11f, -10f)
            lineToRelative(-20f, -255f)
            lineToRelative(31f, 31f)
            lineToRelative(54f, 21f)
            horizontalLineToRelative(16f)
            close()
            moveTo(346f, 381f)
            quadToRelative(19f, -19f, 31.5f, -46f)
            reflectiveQuadToRelative(15.5f, -58f)
            horizontalLineToRelative(21f)
            quadToRelative(-4f, 41f, -21.5f, 75f)
            reflectiveQuadToRelative(-44.5f, 55f)
            close()
            moveTo(278f, 415f)
            quadToRelative(-7f, 1f, -12.5f, 1f)
            reflectiveQuadToRelative(-11.5f, -1f)
            lineToRelative(-4f, 21f)
            quadToRelative(8f, 1f, 15.5f, 1f)
            reflectiveQuadToRelative(17.5f, -1f)
            close()
            moveTo(139f, 277f)
            quadToRelative(4f, 31f, 16.5f, 58f)
            reflectiveQuadToRelative(31.5f, 46f)
            lineToRelative(-2f, 26f)
            quadToRelative(-28f, -21f, -45.5f, -55f)
            reflectiveQuadToRelative(-21.5f, -75f)
            horizontalLineToRelative(21f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
