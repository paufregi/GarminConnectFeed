package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Running: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Running",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(177f, 346f)
            lineToRelative(-103f, 64f)
            lineToRelative(12f, 12f)
            lineToRelative(125f, -53f)
            lineToRelative(36f, -45f)
            lineToRelative(-46f, -36f)
            close()
            moveTo(394f, 244f)
            lineToRelative(83f, -71f)
            lineToRelative(-12f, -12f)
            lineToRelative(-71f, 36f)
            lineToRelative(-71f, -71f)
            lineToRelative(-95f, -12f)
            lineToRelative(-59f, 71f)
            lineToRelative(12f, 12f)
            lineToRelative(59f, -48f)
            lineToRelative(40f, 9f)
            lineToRelative(-28f, 36f)
            quadToRelative(-30f, 38f, -36f, 48f)
            reflectiveQuadToRelative(-3f, 16f)
            quadToRelative(2f, 4f, 6f, 6f)
            lineToRelative(85f, 70f)
            lineToRelative(-40f, 123f)
            lineToRelative(11f, 12f)
            lineToRelative(87f, -136f)
            lineToRelative(-59f, -79f)
            lineToRelative(36f, -48f)
            close()
            moveTo(394f, 137f)
            quadToRelative(14f, 0f, 26f, -8f)
            reflectiveQuadToRelative(17.5f, -21f)
            reflectiveQuadToRelative(2.5f, -27.5f)
            reflectiveQuadToRelative(-12.5f, -24f)
            reflectiveQuadToRelative(-24f, -12.5f)
            reflectiveQuadToRelative(-27.5f, 2.5f)
            reflectiveQuadToRelative(-21f, 17.5f)
            reflectiveQuadToRelative(-8f, 26f)
            quadToRelative(0f, 19f, 14f, 33f)
            reflectiveQuadToRelative(33f, 14f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
