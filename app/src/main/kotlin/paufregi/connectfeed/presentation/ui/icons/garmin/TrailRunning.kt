package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.TrailRunning: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.TrailRunning",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(390f, 391f)
            lineToRelative(-79f, 33f)
            lineToRelative(-47f, -13f)
            lineToRelative(77f, -115f)
            lineToRelative(-51f, -68f)
            lineToRelative(31f, -42f)
            lineToRelative(51f, 31f)
            lineToRelative(62f, -62f)
            lineToRelative(-11f, -11f)
            lineToRelative(-51f, 32f)
            lineToRelative(-71f, -61f)
            lineToRelative(-80f, -9f)
            lineToRelative(-49f, 59f)
            lineToRelative(10f, 10f)
            lineToRelative(49f, -40f)
            lineToRelative(41f, 10f)
            lineToRelative(-25f, 31f)
            quadToRelative(-25f, 33f, -30f, 42f)
            quadToRelative(-7f, 12f, 1f, 19f)
            lineToRelative(75f, 62f)
            lineToRelative(-40f, 109f)
            lineToRelative(-21f, -6f)
            lineToRelative(-78f, 34f)
            lineToRelative(-101f, -12f)
            verticalLineToRelative(34f)
            lineToRelative(101f, 11f)
            lineToRelative(78f, -33f)
            lineToRelative(79f, 22f)
            lineToRelative(79f, -34f)
            lineToRelative(89f, 34f)
            verticalLineToRelative(-34f)
            close()
            moveTo(241f, 287f)
            lineToRelative(-40f, -31f)
            lineToRelative(-20f, 52f)
            lineToRelative(-83f, 60f)
            lineToRelative(11f, 12f)
            lineToRelative(102f, -53f)
            close()
            moveTo(383f, 115f)
            quadToRelative(10f, -7f, 14.5f, -18f)
            reflectiveQuadToRelative(2.5f, -22.5f)
            reflectiveQuadToRelative(-10.5f, -20f)
            reflectiveQuadToRelative(-20f, -11f)
            reflectiveQuadToRelative(-23f, 2f)
            reflectiveQuadToRelative(-17.5f, 14.5f)
            quadToRelative(-8f, 12f, -6.5f, 26f)
            reflectiveQuadToRelative(11f, 24f)
            reflectiveQuadToRelative(24f, 11f)
            reflectiveQuadToRelative(25.5f, -6f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
