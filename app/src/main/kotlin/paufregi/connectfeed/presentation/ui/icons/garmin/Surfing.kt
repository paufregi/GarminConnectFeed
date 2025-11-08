package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Surfing: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Surfing",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(427f, 424f)
            lineToRelative(-41f, -17f)
            quadToRelative(-2f, -9f, -16f, -44f)
            lineToRelative(-18f, -45f)
            quadToRelative(-16f, -27f, -36f, -49f)
            lineToRelative(36f, -37f)
            lineToRelative(37f, 49f)
            lineToRelative(73f, 37f)
            lineToRelative(12f, -9f)
            lineToRelative(-66f, -56f)
            lineToRelative(-19f, -58f)
            lineToRelative(-73f, -61f)
            lineToRelative(-63f, -25f)
            lineToRelative(-89f, -54f)
            lineToRelative(-3f, 17f)
            lineToRelative(69f, 63f)
            lineToRelative(61f, 36f)
            lineToRelative(-37f, 61f)
            lineToRelative(-24f, -24f)
            quadToRelative(-13f, -11f, -26.5f, -9.5f)
            reflectiveQuadToRelative(-18.5f, 14.5f)
            lineToRelative(-42f, 123f)
            lineToRelative(-45f, -13f)
            lineToRelative(-45f, -10f)
            lineToRelative(20f, 41f)
            quadToRelative(8f, 8f, 19f, 11f)
            lineToRelative(99f, 31f)
            lineToRelative(191f, 56f)
            quadToRelative(7f, 3f, 45f, 5f)
            quadToRelative(42f, 2f, 52f, -2f)
            quadToRelative(-25f, -18f, -52f, -31f)
            close()
            moveTo(161f, 342f)
            lineToRelative(44f, -71f)
            lineToRelative(43f, 23f)
            lineToRelative(59f, 25f)
            quadToRelative(2f, 4f, 25f, 37f)
            quadToRelative(21f, 31f, 32f, 47f)
            lineToRelative(-163f, -50f)
            close()
            moveTo(448f, 144f)
            quadToRelative(5f, -14f, 1f, -28f)
            reflectiveQuadToRelative(-16f, -22.5f)
            reflectiveQuadToRelative(-26.5f, -8f)
            reflectiveQuadToRelative(-26f, 10f)
            reflectiveQuadToRelative(-14.5f, 24f)
            reflectiveQuadToRelative(3.5f, 27.5f)
            reflectiveQuadToRelative(19.5f, 20f)
            quadToRelative(16f, 8f, 34f, 1f)
            reflectiveQuadToRelative(25f, -24f)
            close()
            moveTo(82f, 425f)
            lineToRelative(-22f, 4f)
            lineToRelative(37f, -47f)
            lineToRelative(42f, 14f)
            quadToRelative(-24f, 24f, -57f, 29f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
