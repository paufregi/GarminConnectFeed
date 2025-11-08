package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Activity: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Activity",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(437f, 61f)
            lineToRelative(-96f, 107f)
            quadToRelative(-5f, 6f, -8f, 12f)
            lineToRelative(-6f, 14f)
            quadToRelative(-4f, 8f, -5.5f, 13.5f)
            reflectiveQuadToRelative(-0.5f, 17.5f)
            quadToRelative(0f, 26f, 19f, 133f)
            lineToRelative(19f, 102f)
            lineToRelative(-14f, 9f)
            lineToRelative(-13f, -9f)
            lineToRelative(-52f, -156f)
            lineToRelative(-3f, -6f)
            quadToRelative(-4f, -6f, -11f, -6f)
            reflectiveQuadToRelative(-11f, 6f)
            quadToRelative(-2f, 3f, -3f, 6f)
            lineToRelative(-52f, 156f)
            lineToRelative(-15f, 9f)
            lineToRelative(-12f, -9f)
            lineToRelative(19f, -101f)
            quadToRelative(19f, -106f, 19f, -134f)
            quadToRelative(2f, -30f, -20f, -59f)
            lineToRelative(-96f, -104f)
            lineToRelative(14f, -12f)
            lineToRelative(126f, 94f)
            horizontalLineToRelative(64f)
            lineToRelative(124f, -95f)
            close()
            moveTo(307f, 83f)
            quadToRelative(0f, -17f, -12f, -28.5f)
            reflectiveQuadToRelative(-29f, -11.5f)
            quadToRelative(-19f, 0f, -31.5f, 14.5f)
            reflectiveQuadToRelative(-9.5f, 33.5f)
            quadToRelative(3f, 11f, 12f, 20f)
            reflectiveQuadToRelative(21f, 11f)
            quadToRelative(18f, 4f, 33.5f, -8f)
            reflectiveQuadToRelative(15.5f, -31f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
