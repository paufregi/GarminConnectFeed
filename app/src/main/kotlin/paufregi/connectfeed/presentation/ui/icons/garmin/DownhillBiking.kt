package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.DownhillBiking: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.DownhillBiking",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(220f, 181f)
            lineToRelative(58f, 68f)
            lineToRelative(-21f, 97f)
            lineToRelative(15f, -3f)
            lineToRelative(46f, -93f)
            lineToRelative(-36f, -51f)
            lineToRelative(43f, -32f)
            lineToRelative(35f, 48f)
            lineToRelative(74f, 7f)
            lineToRelative(-4f, -15f)
            lineToRelative(-54f, -17f)
            lineToRelative(-27f, -61f)
            lineToRelative(-38f, -23f)
            lineToRelative(-88f, 58f)
            quadToRelative(-4f, 3f, -5f, 8f)
            reflectiveQuadToRelative(2f, 9f)
            verticalLineToRelative(0f)
            close()
            moveTo(316f, 305f)
            quadToRelative(-20f, 32f, -12f, 66f)
            quadToRelative(7f, 30f, 33f, 50.5f)
            reflectiveQuadToRelative(57f, 19.5f)
            quadToRelative(35f, -1f, 61f, -28f)
            quadToRelative(30f, -34f, 24f, -73f)
            quadToRelative(-5f, -34f, -34.5f, -57f)
            reflectiveQuadToRelative(-64.5f, -19f)
            quadToRelative(-38f, 4f, -64f, 41f)
            close()
            moveTo(426f, 296f)
            quadToRelative(24f, 15f, 30f, 40f)
            quadToRelative(5f, 23f, -6.5f, 45.5f)
            reflectiveQuadToRelative(-32.5f, 31.5f)
            quadToRelative(-23f, 11f, -50f, 1f)
            quadToRelative(-31f, -14f, -40f, -42f)
            quadToRelative(-8f, -24f, 5f, -49.5f)
            reflectiveQuadToRelative(37f, -33.5f)
            quadToRelative(28f, -10f, 57f, 7f)
            close()
            moveTo(234f, 331f)
            quadToRelative(19f, -32f, 12f, -65f)
            quadToRelative(-7f, -31f, -33f, -51f)
            reflectiveQuadToRelative(-57f, -20f)
            quadToRelative(-35f, 1f, -61f, 28f)
            quadToRelative(-30f, 34f, -24f, 73f)
            quadToRelative(5f, 34f, 34.5f, 57f)
            reflectiveQuadToRelative(63.5f, 20f)
            quadToRelative(39f, -4f, 65f, -42f)
            close()
            moveTo(123f, 340f)
            quadToRelative(-23f, -15f, -29f, -40f)
            quadToRelative(-5f, -23f, 6f, -45f)
            reflectiveQuadToRelative(33f, -32f)
            quadToRelative(23f, -11f, 50f, -1f)
            quadToRelative(31f, 14f, 40f, 42f)
            quadToRelative(7f, 25f, -5.5f, 50f)
            reflectiveQuadToRelative(-37.5f, 33f)
            quadToRelative(-27f, 10f, -57f, -7f)
            close()
            moveTo(174f, 401f)
            lineToRelative(-88f, -23f)
            lineToRelative(-34f, 34f)
            verticalLineToRelative(44f)
            horizontalLineToRelative(288f)
            lineToRelative(-55f, -44f)
            lineToRelative(-34f, 11f)
            lineToRelative(-33f, -33f)
            lineToRelative(-44f, 11f)
            verticalLineToRelative(0f)
            close()
            moveTo(415f, 96f)
            quadToRelative(0f, 13f, -9f, 22.5f)
            reflectiveQuadToRelative(-22.5f, 9.5f)
            reflectiveQuadToRelative(-23f, -9.5f)
            reflectiveQuadToRelative(-9.5f, -22.5f)
            reflectiveQuadToRelative(9.5f, -22.5f)
            reflectiveQuadToRelative(23f, -9.5f)
            reflectiveQuadToRelative(22.5f, 9.5f)
            reflectiveQuadToRelative(9f, 22.5f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
