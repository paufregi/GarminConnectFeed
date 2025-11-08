package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Snowboarding: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Snowboarding",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(257f, 114f)
            quadToRelative(-10f, -10f, -12.5f, -24f)
            reflectiveQuadToRelative(4.5f, -26f)
            reflectiveQuadToRelative(20f, -17.5f)
            reflectiveQuadToRelative(26.5f, -2.5f)
            reflectiveQuadToRelative(22f, 14.5f)
            reflectiveQuadToRelative(8.5f, 25.5f)
            quadToRelative(0f, 17f, -12f, 28f)
            reflectiveQuadToRelative(-28f, 12f)
            reflectiveQuadToRelative(-29f, -10f)
            close()
            moveTo(430f, 171f)
            horizontalLineToRelative(-82f)
            lineToRelative(-46f, -21f)
            lineToRelative(-93f, -14f)
            lineToRelative(-61f, 25f)
            lineToRelative(-95f, 16f)
            lineToRelative(9f, 13f)
            lineToRelative(89f, 4f)
            lineToRelative(58f, -10f)
            lineToRelative(12f, 70f)
            horizontalLineToRelative(-32f)
            quadToRelative(-15f, 0f, -22f, 6.5f)
            reflectiveQuadToRelative(-3f, 18.5f)
            lineToRelative(44f, 113f)
            quadToRelative(-60f, 26f, -65f, 27f)
            quadToRelative(-23f, 4f, -46f, 0f)
            lineToRelative(-2f, 1f)
            quadToRelative(31f, 18f, 65f, 32f)
            quadToRelative(11f, 1f, 20f, -4f)
            quadToRelative(20f, -6f, 177f, -77f)
            verticalLineToRelative(0f)
            quadToRelative(78f, -35f, 85f, -37f)
            quadToRelative(24f, -10f, 37f, -32f)
            quadToRelative(1f, -3f, -1f, -6f)
            lineToRelative(-31f, -20f)
            quadToRelative(-3f, -1f, -5f, 1f)
            quadToRelative(-10f, 16f, -27f, 23f)
            quadToRelative(-3f, 1f, -50f, 21f)
            quadToRelative(-19f, -39f, -38f, -74f)
            lineToRelative(-4f, -8f)
            quadToRelative(-6f, -11f, -9f, -15f)
            quadToRelative(-5f, -6f, -9f, -4f)
            quadToRelative(-19f, 10f, -24f, 22f)
            lineToRelative(-3f, 5f)
            quadToRelative(3f, 26f, 10f, 49f)
            lineToRelative(18f, -10f)
            quadToRelative(18f, 19f, 42f, 43f)
            lineToRelative(-125f, 55f)
            lineToRelative(-9f, -77f)
            lineToRelative(29f, 9f)
            lineToRelative(19f, -7f)
            quadToRelative(-11f, -34f, -11f, -69f)
            quadToRelative(5f, -18f, 28f, -32f)
            quadToRelative(11f, -7f, 21f, -10f)
            verticalLineToRelative(-7f)
            lineToRelative(46f, 16f)
            lineToRelative(70f, -16f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
