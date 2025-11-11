package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.StandUpPaddling: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.StandUpPaddling",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(298f, 107f)
            quadToRelative(-12f, 0f, -21f, -8f)
            reflectiveQuadToRelative(-10.5f, -20f)
            reflectiveQuadToRelative(4.5f, -22f)
            quadToRelative(6f, -8f, 15f, -12f)
            reflectiveQuadToRelative(18.5f, -2f)
            reflectiveQuadToRelative(16.5f, 9f)
            reflectiveQuadToRelative(8f, 16f)
            quadToRelative(3f, 15f, -6.5f, 27f)
            reflectiveQuadToRelative(-24.5f, 12f)
            close()
            moveTo(199f, 306f)
            lineToRelative(6f, -56f)
            lineToRelative(31f, 35f)
            lineToRelative(-8f, 38f)
            lineToRelative(-35f, 53f)
            lineToRelative(60f, 2f)
            lineToRelative(86f, -178f)
            lineToRelative(-32f, -8f)
            lineToRelative(-16f, -16f)
            lineToRelative(-19f, 51f)
            lineToRelative(16f, 27f)
            lineToRelative(-20f, 38f)
            lineToRelative(-54f, -63f)
            quadToRelative(-4f, -4f, -5f, -9.5f)
            reflectiveQuadToRelative(2f, -10.5f)
            lineToRelative(43f, -102f)
            lineToRelative(45f, 22f)
            lineToRelative(26f, 40f)
            lineToRelative(24f, 11f)
            lineToRelative(11f, -23f)
            horizontalLineToRelative(21f)
            lineToRelative(-15f, 31f)
            lineToRelative(15f, 7f)
            lineToRelative(-5f, 13f)
            lineToRelative(-18f, -4f)
            lineToRelative(-49f, 100f)
            lineToRelative(12f, 73f)
            lineToRelative(122f, -3f)
            lineToRelative(20f, 4f)
            quadToRelative(-38f, 26f, -85f, 27f)
            horizontalLineToRelative(-118f)
            lineToRelative(-5f, 10f)
            lineToRelative(10f, 12f)
            lineToRelative(-20f, 42f)
            horizontalLineToRelative(-52f)
            lineToRelative(23f, -44f)
            lineToRelative(19f, -11f)
            lineToRelative(4f, -9f)
            horizontalLineToRelative(-90f)
            quadToRelative(-5f, 9f, -13.5f, 14.5f)
            reflectiveQuadToRelative(-18.5f, 7.5f)
            horizontalLineToRelative(-22f)
            lineToRelative(10f, -10f)
            quadToRelative(10f, -10f, 10f, -12f)
            horizontalLineToRelative(-20f)
            lineToRelative(-32f, -10f)
            lineToRelative(32f, -28f)
            quadToRelative(39f, 5f, 78f, 7f)
            close()
            moveTo(274f, 378f)
            quadToRelative(7f, 0f, 21f, -1f)
            horizontalLineToRelative(8f)
            lineToRelative(-11f, -37f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
