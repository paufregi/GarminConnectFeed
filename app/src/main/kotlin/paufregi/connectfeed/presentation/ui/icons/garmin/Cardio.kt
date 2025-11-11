package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Cardio: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Cardio",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(221f, 332f)
            lineToRelative(-52f, -26f)
            lineToRelative(-11f, 58f)
            lineToRelative(-53f, 92f)
            lineToRelative(13f, 12f)
            lineToRelative(73f, -86f)
            close()
            moveTo(407f, 427f)
            horizontalLineToRelative(-111f)
            lineToRelative(14f, -118f)
            lineToRelative(-67f, -53f)
            lineToRelative(21f, -65f)
            lineToRelative(52f, 29f)
            lineToRelative(56f, -83f)
            horizontalLineToRelative(-21f)
            lineToRelative(-41f, 36f)
            lineToRelative(-45f, -25f)
            lineToRelative(-65f, -10f)
            lineToRelative(-70f, 36f)
            lineToRelative(-12f, 83f)
            lineToRelative(17f, 9f)
            lineToRelative(19f, -69f)
            lineToRelative(43f, -12f)
            lineToRelative(-16f, 64f)
            quadToRelative(-3f, 15f, -3f, 21f)
            quadToRelative(1f, 9f, 7f, 12f)
            lineToRelative(91f, 49f)
            verticalLineToRelative(91f)
            lineToRelative(12f, 3f)
            horizontalLineToRelative(-110f)
            lineToRelative(-4f, 2f)
            quadToRelative(-4f, 2f, -6f, 4.5f)
            reflectiveQuadToRelative(-6f, 10.5f)
            quadToRelative(-4f, 13f, -4f, 27f)
            horizontalLineToRelative(21f)
            lineToRelative(6f, -11f)
            horizontalLineToRelative(214f)
            lineToRelative(6f, 11f)
            horizontalLineToRelative(22f)
            quadToRelative(-3f, -15f, -6f, -23f)
            quadToRelative(-5f, -13f, -14f, -19f)
            close()
            moveTo(247f, 118f)
            quadToRelative(18f, 0f, 29f, -14f)
            reflectiveQuadToRelative(8f, -31f)
            quadToRelative(-2f, -11f, -10f, -19f)
            reflectiveQuadToRelative(-19.5f, -10.5f)
            reflectiveQuadToRelative(-21.5f, 2f)
            reflectiveQuadToRelative(-16.5f, 14f)
            reflectiveQuadToRelative(-6.5f, 20.5f)
            quadToRelative(0f, 15f, 11f, 26.5f)
            reflectiveQuadToRelative(26f, 11.5f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
