package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.UltraRun: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.UltraRun",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(191f, 205f)
            lineToRelative(33f, -95f)
            lineToRelative(-27f, -5f)
            lineToRelative(-18f, 26f)
            quadToRelative(-18f, 29f, -20f, 41.5f)
            reflectiveQuadToRelative(0f, 17f)
            reflectiveQuadToRelative(11f, 7.5f)
            close()
            moveTo(210f, 268f)
            lineToRelative(91f, 39f)
            lineToRelative(22f, 77f)
            lineToRelative(19f, 10f)
            lineToRelative(-11f, -111f)
            lineToRelative(-62f, -44f)
            lineToRelative(17f, -56f)
            lineToRelative(21f, 9f)
            lineToRelative(57f, 3f)
            lineToRelative(13f, -14f)
            lineToRelative(-60f, -11f)
            lineToRelative(-46f, -47f)
            lineToRelative(-27f, -10f)
            lineToRelative(-43f, 132f)
            quadToRelative(-2f, 9f, 0f, 14f)
            reflectiveQuadToRelative(10f, 9f)
            horizontalLineToRelative(-1f)
            close()
            moveTo(388f, 401f)
            lineToRelative(-57f, 35f)
            lineToRelative(-79f, -28f)
            lineToRelative(-84f, 16f)
            lineToRelative(-96f, -45f)
            lineToRelative(-10f, 30f)
            lineToRelative(106f, 49f)
            lineToRelative(84f, -17f)
            lineToRelative(79f, 28f)
            lineToRelative(63f, -40f)
            lineToRelative(67f, -14f)
            lineToRelative(9f, -27f)
            close()
            moveTo(233f, 304f)
            lineToRelative(-36f, -19f)
            lineToRelative(-26f, 40f)
            horizontalLineToRelative(-68f)
            lineToRelative(-14f, 12f)
            lineToRelative(91f, 22f)
            close()
            moveTo(331f, 78f)
            quadToRelative(0f, 15f, -10.5f, 25.5f)
            reflectiveQuadToRelative(-25.5f, 10.5f)
            reflectiveQuadToRelative(-25.5f, -10.5f)
            reflectiveQuadToRelative(-10.5f, -25f)
            reflectiveQuadToRelative(10.5f, -25f)
            reflectiveQuadToRelative(25.5f, -10.5f)
            reflectiveQuadToRelative(25.5f, 10.5f)
            reflectiveQuadToRelative(10.5f, 24.5f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
