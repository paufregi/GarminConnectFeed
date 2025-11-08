package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.OpenWaterSwimming: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.OpenWaterSwimming",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(99f, 256f)
            lineToRelative(56f, -44f)
            lineToRelative(122f, -124f)
            lineToRelative(106f, 57f)
            lineToRelative(10f, 25f)
            lineToRelative(-105f, -41f)
            lineToRelative(-62f, 93f)
            lineToRelative(18f, 45f)
            lineToRelative(-89f, -24f)
            close()
            moveTo(356f, 252f)
            quadToRelative(3f, -17f, -5.5f, -32f)
            reflectiveQuadToRelative(-24.5f, -20f)
            quadToRelative(-11f, -4f, -22f, -2f)
            reflectiveQuadToRelative(-20f, 9f)
            quadToRelative(-13f, 11f, -16f, 28f)
            reflectiveQuadToRelative(6f, 31f)
            close()
            moveTo(267f, 351f)
            lineToRelative(124f, -22f)
            lineToRelative(78f, 19f)
            lineToRelative(-4f, 23f)
            lineToRelative(-74f, -20f)
            lineToRelative(-124f, 24f)
            lineToRelative(-112f, -24f)
            lineToRelative(-87f, 20f)
            lineToRelative(-5f, -23f)
            lineToRelative(92f, -20f)
            close()
            moveTo(391f, 277f)
            lineToRelative(-124f, 24f)
            lineToRelative(-112f, -24f)
            lineToRelative(-102f, 24f)
            lineToRelative(5f, 22f)
            lineToRelative(97f, -22f)
            lineToRelative(112f, 23f)
            lineToRelative(124f, -23f)
            lineToRelative(84f, 21f)
            lineToRelative(4f, -22f)
            close()
            moveTo(266f, 401f)
            lineToRelative(124f, -23f)
            lineToRelative(69f, 17f)
            lineToRelative(-4f, 23f)
            lineToRelative(-65f, -16f)
            lineToRelative(-124f, 22f)
            lineToRelative(-112f, -23f)
            lineToRelative(-76f, 17f)
            lineToRelative(-5f, -23f)
            lineToRelative(81f, -18f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
