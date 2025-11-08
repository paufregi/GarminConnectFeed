package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Kayaking: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Kayaking",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(285f, 170f)
            quadToRelative(-12f, -2f, -20.5f, -12f)
            reflectiveQuadToRelative(-9.5f, -23f)
            reflectiveQuadToRelative(6.5f, -23.5f)
            reflectiveQuadToRelative(20f, -14f)
            reflectiveQuadToRelative(24.5f, 1f)
            reflectiveQuadToRelative(18.5f, 15.5f)
            reflectiveQuadToRelative(5f, 24f)
            reflectiveQuadToRelative(-10.5f, 22f)
            quadToRelative(-7f, 6f, -16f, 9f)
            reflectiveQuadToRelative(-18f, 1f)
            close()
            moveTo(465f, 125f)
            lineToRelative(-57f, 18f)
            lineToRelative(-8f, 17f)
            lineToRelative(-111f, 37f)
            lineToRelative(-35f, -15f)
            lineToRelative(-66f, -11f)
            lineToRelative(-45f, 68f)
            lineToRelative(5f, 5f)
            lineToRelative(-27f, 8f)
            lineToRelative(-10f, -4f)
            lineToRelative(-58f, 20f)
            lineToRelative(14f, 38f)
            lineToRelative(56f, -18f)
            lineToRelative(8f, -16f)
            lineToRelative(89f, -30f)
            lineToRelative(-24f, 50f)
            lineToRelative(70f, 21f)
            lineToRelative(32f, -65f)
            lineToRelative(45f, 24f)
            lineToRelative(57f, -89f)
            lineToRelative(11f, -4f)
            lineToRelative(10f, 4f)
            lineToRelative(58f, -19f)
            close()
            moveTo(367f, 193f)
            lineToRelative(-35f, 35f)
            lineToRelative(-19f, -17f)
            close()
            moveTo(232f, 216f)
            lineToRelative(-64f, 21f)
            lineToRelative(33f, -34f)
            close()
            moveTo(385f, 361f)
            lineToRelative(-118f, 22f)
            lineToRelative(-107f, -22f)
            lineToRelative(-88f, 19f)
            lineToRelative(5f, 22f)
            lineToRelative(83f, -19f)
            lineToRelative(107f, 23f)
            lineToRelative(118f, -23f)
            lineToRelative(71f, 19f)
            lineToRelative(4f, -22f)
            close()
            moveTo(267f, 335f)
            lineToRelative(118f, -22f)
            lineToRelative(85f, 21f)
            lineToRelative(-5f, 21f)
            lineToRelative(-80f, -20f)
            lineToRelative(-118f, 23f)
            lineToRelative(-107f, -23f)
            lineToRelative(-92f, 20f)
            lineToRelative(-6f, -21f)
            lineToRelative(98f, -21f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
