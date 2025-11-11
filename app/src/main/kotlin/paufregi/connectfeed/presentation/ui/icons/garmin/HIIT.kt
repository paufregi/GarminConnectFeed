package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.HIIT: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.HIIT",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(434f, 297f)
            lineToRelative(-25f, -25f)
            lineToRelative(-12f, 12f)
            lineToRelative(25f, 25f)
            close()
            moveTo(329f, 277f)
            quadToRelative(-29f, 0f, -53f, 16.5f)
            reflectiveQuadToRelative(-35f, 43f)
            reflectiveQuadToRelative(-5.5f, 55.5f)
            reflectiveQuadToRelative(26f, 49.5f)
            reflectiveQuadToRelative(49f, 26f)
            reflectiveQuadToRelative(55.5f, -5.5f)
            quadToRelative(30f, -12f, 45.5f, -41f)
            reflectiveQuadToRelative(12f, -61f)
            reflectiveQuadToRelative(-26.5f, -55f)
            quadToRelative(-13f, -13f, -31f, -20.5f)
            reflectiveQuadToRelative(-37f, -7.5f)
            close()
            moveTo(288f, 435f)
            quadToRelative(-19f, -12f, -27.5f, -33f)
            reflectiveQuadToRelative(-4f, -43f)
            reflectiveQuadToRelative(20.5f, -38f)
            reflectiveQuadToRelative(38f, -20.5f)
            reflectiveQuadToRelative(42.5f, 4.5f)
            reflectiveQuadToRelative(33.5f, 27.5f)
            reflectiveQuadToRelative(13f, 41f)
            reflectiveQuadToRelative(-13f, 41.5f)
            reflectiveQuadToRelative(-33f, 27f)
            quadToRelative(-17f, 7f, -36f, 5.5f)
            reflectiveQuadToRelative(-34f, -12.5f)
            close()
            moveTo(338f, 361f)
            lineToRelative(-5f, -53f)
            horizontalLineToRelative(-7f)
            lineToRelative(-6f, 53f)
            quadToRelative(-5f, 3f, -7.5f, 8.5f)
            reflectiveQuadToRelative(-1f, 11.5f)
            reflectiveQuadToRelative(6.5f, 10f)
            reflectiveQuadToRelative(11f, 4f)
            reflectiveQuadToRelative(11f, -4f)
            reflectiveQuadToRelative(6.5f, -9.5f)
            reflectiveQuadToRelative(-1f, -11.5f)
            reflectiveQuadToRelative(-7.5f, -9f)
            close()
            moveTo(290f, 266f)
            horizontalLineToRelative(79f)
            verticalLineToRelative(-22f)
            horizontalLineToRelative(-79f)
            verticalLineToRelative(22f)
            close()
            moveTo(152f, 191f)
            lineToRelative(36f, -15f)
            lineToRelative(-17f, 66f)
            quadToRelative(-2f, 11f, -2f, 16f)
            quadToRelative(0f, 6f, 6f, 9f)
            lineToRelative(63f, 30f)
            quadToRelative(13f, -18f, 31f, -31f)
            lineToRelative(-27f, -22f)
            lineToRelative(10f, -58f)
            lineToRelative(50f, 27f)
            lineToRelative(54f, -86f)
            horizontalLineToRelative(-11f)
            lineToRelative(-50f, 38f)
            lineToRelative(-41f, -23f)
            lineToRelative(-59f, -5f)
            lineToRelative(-64f, 22f)
            lineToRelative(-12f, 85f)
            lineToRelative(10f, 10f)
            close()
            moveTo(265f, 111f)
            quadToRelative(15f, -10f, 16.5f, -27.5f)
            reflectiveQuadToRelative(-10.5f, -30f)
            reflectiveQuadToRelative(-30f, -10.5f)
            reflectiveQuadToRelative(-27f, 16f)
            quadToRelative(-8f, 11f, -6.5f, 24.5f)
            reflectiveQuadToRelative(11f, 23f)
            reflectiveQuadToRelative(22.5f, 10.5f)
            reflectiveQuadToRelative(24f, -6f)
            close()
            moveTo(150f, 349f)
            lineToRelative(-52f, 97f)
            lineToRelative(13f, 12f)
            lineToRelative(76f, -96f)
            lineToRelative(30f, -47f)
            lineToRelative(-55f, -27f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
