package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Swimming: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Swimming",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(155f, 237f)
            lineToRelative(-56f, 44f)
            lineToRelative(56f, -14f)
            lineToRelative(89f, 25f)
            lineToRelative(-18f, -45f)
            lineToRelative(62f, -93f)
            lineToRelative(105f, 40f)
            lineToRelative(-10f, -24f)
            lineToRelative(-106f, -58f)
            close()
            moveTo(356f, 276f)
            lineToRelative(-82f, 15f)
            quadToRelative(-6f, -10f, -6.5f, -21f)
            reflectiveQuadToRelative(3.5f, -21f)
            reflectiveQuadToRelative(13f, -17f)
            reflectiveQuadToRelative(20f, -9f)
            reflectiveQuadToRelative(22f, 1f)
            quadToRelative(16f, 6f, 24.5f, 21f)
            reflectiveQuadToRelative(5.5f, 31f)
            close()
            moveTo(391f, 354f)
            lineToRelative(-124f, 22f)
            lineToRelative(-112f, -23f)
            lineToRelative(-92f, 20f)
            lineToRelative(5f, 22f)
            lineToRelative(87f, -19f)
            lineToRelative(112f, 24f)
            lineToRelative(124f, -24f)
            lineToRelative(74f, 19f)
            lineToRelative(4f, -22f)
            close()
            moveTo(267f, 326f)
            lineToRelative(124f, -24f)
            lineToRelative(88f, 22f)
            lineToRelative(-4f, 23f)
            lineToRelative(-84f, -21f)
            lineToRelative(-124f, 23f)
            lineToRelative(-112f, -23f)
            lineToRelative(-97f, 22f)
            lineToRelative(-5f, -22f)
            lineToRelative(102f, -24f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
