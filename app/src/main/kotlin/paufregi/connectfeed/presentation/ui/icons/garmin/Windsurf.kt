package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Windsurf: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Windsurf",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(290f, 426f)
            lineToRelative(-41f, -90f)
            lineToRelative(-76f, -38f)
            lineToRelative(-15f, -52f)
            lineToRelative(44f, -30f)
            lineToRelative(27f, -56f)
            lineToRelative(-8f, -22f)
            lineToRelative(-41f, 57f)
            lineToRelative(-94f, 32f)
            lineToRelative(16f, 51f)
            quadToRelative(17f, 53f, 18f, 60f)
            lineToRelative(101f, 26f)
            lineToRelative(20f, 29f)
            quadToRelative(21f, 31f, 28f, 43f)
            close()
            moveTo(468f, 311f)
            quadToRelative(-3f, -12f, -7f, -24f)
            quadToRelative(-11f, -30f, -26f, -59f)
            quadToRelative(-21f, -40f, -49f, -73f)
            quadToRelative(-34f, -41f, -75f, -69f)
            quadToRelative(-49f, -32f, -107f, -43f)
            lineToRelative(133f, 361f)
            close()
            moveTo(246f, 469f)
            horizontalLineToRelative(96f)
            lineToRelative(20f, -32f)
            horizontalLineToRelative(-42f)
            close()
            moveTo(133f, 143f)
            quadToRelative(-6f, -8f, -15f, -12f)
            reflectiveQuadToRelative(-18.5f, -2f)
            reflectiveQuadToRelative(-16f, 9f)
            reflectiveQuadToRelative(-8.5f, 16.5f)
            reflectiveQuadToRelative(1.5f, 18.5f)
            reflectiveQuadToRelative(11.5f, 14.5f)
            reflectiveQuadToRelative(18f, 5.5f)
            quadToRelative(13f, 0f, 22f, -9f)
            reflectiveQuadToRelative(10f, -20.5f)
            reflectiveQuadToRelative(-5f, -20.5f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
