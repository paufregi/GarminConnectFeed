package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.MountainBiking: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.MountainBiking",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(446f, 391f)
            lineToRelative(-90f, 22f)
            lineToRelative(-45f, -11f)
            lineToRelative(-34f, 34f)
            lineToRelative(-33f, -12f)
            lineToRelative(-57f, 45f)
            horizontalLineToRelative(292f)
            verticalLineToRelative(-45f)
            close()
            moveTo(139f, 236f)
            lineToRelative(87f, 27f)
            lineToRelative(34f, 94f)
            lineToRelative(12f, -11f)
            lineToRelative(-12f, -105f)
            lineToRelative(-58f, -24f)
            lineToRelative(19f, -51f)
            lineToRelative(56f, 23f)
            lineToRelative(68f, -34f)
            lineToRelative(-12f, -11f)
            lineToRelative(-56f, 14f)
            lineToRelative(-56f, -37f)
            horizontalLineToRelative(-45f)
            lineToRelative(-44f, 98f)
            quadToRelative(-2f, 5f, 0f, 10f)
            reflectiveQuadToRelative(7f, 7f)
            close()
            moveTo(288f, 290f)
            quadToRelative(0f, 27f, 15.5f, 49.5f)
            reflectiveQuadToRelative(40.5f, 33f)
            reflectiveQuadToRelative(52f, 5f)
            reflectiveQuadToRelative(46f, -24.5f)
            reflectiveQuadToRelative(24.5f, -46f)
            reflectiveQuadToRelative(-5.5f, -52f)
            quadToRelative(-11f, -28f, -37.5f, -42.5f)
            reflectiveQuadToRelative(-56.5f, -11.5f)
            reflectiveQuadToRelative(-52f, 25f)
            quadToRelative(-27f, 26f, -27f, 64f)
            close()
            moveTo(378f, 222f)
            quadToRelative(21f, 0f, 38f, 11.5f)
            reflectiveQuadToRelative(24.5f, 30.5f)
            reflectiveQuadToRelative(3.5f, 39f)
            reflectiveQuadToRelative(-18f, 34.5f)
            reflectiveQuadToRelative(-34.5f, 18.5f)
            reflectiveQuadToRelative(-39f, -4f)
            reflectiveQuadToRelative(-30f, -25f)
            reflectiveQuadToRelative(-11.5f, -37f)
            quadToRelative(0f, -28f, 20f, -48f)
            reflectiveQuadToRelative(47f, -20f)
            close()
            moveTo(232f, 357f)
            quadToRelative(0f, -27f, -15f, -50f)
            reflectiveQuadToRelative(-40f, -33f)
            reflectiveQuadToRelative(-52f, -5f)
            reflectiveQuadToRelative(-46f, 24.5f)
            reflectiveQuadToRelative(-24.5f, 46.5f)
            reflectiveQuadToRelative(5f, 51.5f)
            reflectiveQuadToRelative(33.5f, 40f)
            reflectiveQuadToRelative(49f, 15.5f)
            quadToRelative(38f, 0f, 64f, -26.5f)
            reflectiveQuadToRelative(26f, -63.5f)
            close()
            moveTo(142f, 424f)
            quadToRelative(-20f, 0f, -37f, -11f)
            reflectiveQuadToRelative(-24.5f, -30f)
            reflectiveQuadToRelative(-3.5f, -39f)
            reflectiveQuadToRelative(18f, -34.5f)
            reflectiveQuadToRelative(34.5f, -18.5f)
            reflectiveQuadToRelative(39f, 4f)
            reflectiveQuadToRelative(30f, 25f)
            reflectiveQuadToRelative(11.5f, 37f)
            quadToRelative(0f, 28f, -20f, 47.5f)
            reflectiveQuadToRelative(-48f, 19.5f)
            close()
            moveTo(232f, 110f)
            quadToRelative(16f, 0f, 26f, -12.5f)
            reflectiveQuadToRelative(7f, -27.5f)
            quadToRelative(-2f, -10f, -9f, -17.5f)
            reflectiveQuadToRelative(-17f, -9.5f)
            reflectiveQuadToRelative(-19.5f, 2f)
            reflectiveQuadToRelative(-15.5f, 13.5f)
            reflectiveQuadToRelative(-5f, 21.5f)
            reflectiveQuadToRelative(10f, 21f)
            reflectiveQuadToRelative(23f, 9f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
