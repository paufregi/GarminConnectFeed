package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Multisport: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Multisport",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(252f, 114f)
            quadToRelative(14f, -32f, 43.5f, -51.5f)
            reflectiveQuadToRelative(65.5f, -19.5f)
            quadToRelative(32f, 0f, 59f, 16f)
            reflectiveQuadToRelative(43f, 43f)
            reflectiveQuadToRelative(16f, 59f)
            quadToRelative(0f, 36f, -19.5f, 65.5f)
            reflectiveQuadToRelative(-51.5f, 43.5f)
            verticalLineToRelative(-14f)
            quadToRelative(0f, -12f, -2f, -25f)
            quadToRelative(17f, -12f, 27.5f, -30f)
            reflectiveQuadToRelative(10.5f, -40f)
            quadToRelative(0f, -34f, -24.5f, -58.5f)
            reflectiveQuadToRelative(-58.5f, -24.5f)
            quadToRelative(-22f, 0f, -40f, 10.5f)
            reflectiveQuadToRelative(-30f, 28.5f)
            reflectiveQuadToRelative(-13f, 41f)
            reflectiveQuadToRelative(10.5f, 43.5f)
            reflectiveQuadToRelative(32.5f, 32.5f)
            quadToRelative(4f, 11f, 4f, 22f)
            quadToRelative(0f, 8f, -3f, 17f)
            quadToRelative(-25f, -9f, -44.5f, -28.5f)
            reflectiveQuadToRelative(-28.5f, -44.5f)
            quadToRelative(-7f, -21f, -6.5f, -43f)
            reflectiveQuadToRelative(9.5f, -43f)
            close()
            moveTo(302f, 161f)
            quadToRelative(0f, -8f, 2f, -17f)
            quadToRelative(32f, 11f, 53f, 37f)
            reflectiveQuadToRelative(26f, 58.5f)
            reflectiveQuadToRelative(-8f, 63.5f)
            quadToRelative(-9f, 21f, -25f, 37f)
            reflectiveQuadToRelative(-37f, 25f)
            verticalLineToRelative(-14f)
            quadToRelative(0f, -13f, -2f, -26f)
            quadToRelative(15f, -10f, 25f, -24f)
            quadToRelative(12f, -20f, 12.5f, -43f)
            reflectiveQuadToRelative(-11f, -43f)
            reflectiveQuadToRelative(-31.5f, -32f)
            quadToRelative(-4f, -11f, -4f, -22f)
            close()
            moveTo(228f, 368f)
            quadToRelative(2f, -9f, 2f, -17f)
            quadToRelative(0f, -11f, -4f, -22f)
            quadToRelative(-19f, -11f, -31f, -30.5f)
            reflectiveQuadToRelative(-12f, -42.5f)
            quadToRelative(0f, -21f, 10.5f, -39.5f)
            reflectiveQuadToRelative(27.5f, -30.5f)
            quadToRelative(-2f, -13f, -2f, -25f)
            verticalLineToRelative(-14f)
            quadToRelative(-32f, 14f, -52f, 43.5f)
            reflectiveQuadToRelative(-20f, 65.5f)
            quadToRelative(0f, 39f, 22.5f, 69.5f)
            reflectiveQuadToRelative(58.5f, 42.5f)
            close()
            moveTo(207f, 256f)
            quadToRelative(0f, -8f, 3f, -17f)
            quadToRelative(25f, 9f, 44.5f, 28.5f)
            reflectiveQuadToRelative(28.5f, 44.5f)
            quadToRelative(7f, 21f, 6.5f, 43f)
            reflectiveQuadToRelative(-9.5f, 43f)
            quadToRelative(-14f, 32f, -43.5f, 51.5f)
            reflectiveQuadToRelative(-65.5f, 19.5f)
            quadToRelative(-32f, 0f, -59f, -16f)
            reflectiveQuadToRelative(-43f, -43f)
            reflectiveQuadToRelative(-16f, -59f)
            quadToRelative(0f, -36f, 19.5f, -65.5f)
            reflectiveQuadToRelative(51.5f, -43.5f)
            verticalLineToRelative(14f)
            quadToRelative(0f, 12f, 2f, 25f)
            quadToRelative(-17f, 12f, -27.5f, 30f)
            reflectiveQuadToRelative(-10.5f, 40f)
            quadToRelative(0f, 34f, 24.5f, 58.5f)
            reflectiveQuadToRelative(58.5f, 24.5f)
            quadToRelative(22f, 0f, 40f, -10.5f)
            reflectiveQuadToRelative(30f, -28.5f)
            reflectiveQuadToRelative(13f, -41f)
            reflectiveQuadToRelative(-10.5f, -43.5f)
            reflectiveQuadToRelative(-32.5f, -32.5f)
            quadToRelative(-4f, -11f, -4f, -22f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
