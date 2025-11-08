package paufregi.connectfeed.presentation.ui.icons.garmin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Cycling: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Connect.Cycling",
        defaultWidth = 522.dp,
        defaultHeight = 512.dp,
        viewportWidth = 522f,
        viewportHeight = 512f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(277f, 188f)
            lineToRelative(34f, 33f)
            horizontalLineToRelative(90f)
            lineToRelative(-13f, -17f)
            lineToRelative(-58f, -10f)
            lineToRelative(-40f, -58f)
            lineToRelative(-35f, -16f)
            lineToRelative(-81f, 85f)
            quadToRelative(-4f, 4f, -3.5f, 9.5f)
            reflectiveQuadToRelative(4.5f, 8.5f)
            lineToRelative(80f, 54f)
            verticalLineToRelative(101f)
            horizontalLineToRelative(15f)
            lineToRelative(24f, -110f)
            lineToRelative(-49f, -47f)
            close()
            moveTo(390f, 255f)
            quadToRelative(-27f, 0f, -50f, 15f)
            reflectiveQuadToRelative(-33.5f, 40f)
            reflectiveQuadToRelative(-5f, 52f)
            reflectiveQuadToRelative(24.5f, 46f)
            reflectiveQuadToRelative(46f, 24.5f)
            reflectiveQuadToRelative(52f, -5f)
            reflectiveQuadToRelative(40f, -33f)
            reflectiveQuadToRelative(15f, -49.5f)
            quadToRelative(0f, -37f, -26.5f, -63.5f)
            reflectiveQuadToRelative(-62.5f, -26.5f)
            close()
            moveTo(390f, 412f)
            quadToRelative(-21f, 0f, -38f, -11.5f)
            reflectiveQuadToRelative(-24.5f, -30f)
            reflectiveQuadToRelative(-3.5f, -39f)
            reflectiveQuadToRelative(18f, -34.5f)
            reflectiveQuadToRelative(34.5f, -18f)
            reflectiveQuadToRelative(39f, 3.5f)
            reflectiveQuadToRelative(30f, 24.5f)
            reflectiveQuadToRelative(11.5f, 38f)
            quadToRelative(0f, 27f, -20f, 47f)
            reflectiveQuadToRelative(-47f, 20f)
            close()
            moveTo(142f, 255f)
            quadToRelative(-26f, 0f, -49f, 15f)
            reflectiveQuadToRelative(-33.5f, 40f)
            reflectiveQuadToRelative(-5f, 52f)
            reflectiveQuadToRelative(24.5f, 46f)
            reflectiveQuadToRelative(46f, 24.5f)
            reflectiveQuadToRelative(52f, -5f)
            reflectiveQuadToRelative(40f, -33f)
            reflectiveQuadToRelative(15f, -49.5f)
            quadToRelative(0f, -37f, -26.5f, -63.5f)
            reflectiveQuadToRelative(-63.5f, -26.5f)
            close()
            moveTo(142f, 412f)
            quadToRelative(-20f, 0f, -37f, -11.5f)
            reflectiveQuadToRelative(-24.5f, -30f)
            reflectiveQuadToRelative(-3.5f, -39f)
            reflectiveQuadToRelative(18f, -34.5f)
            reflectiveQuadToRelative(34.5f, -18f)
            reflectiveQuadToRelative(39f, 3.5f)
            reflectiveQuadToRelative(30f, 24.5f)
            reflectiveQuadToRelative(11.5f, 38f)
            quadToRelative(0f, 27f, -20f, 47f)
            reflectiveQuadToRelative(-48f, 20f)
            close()
            moveTo(322f, 131f)
            quadToRelative(10f, 0f, 18.5f, -5.5f)
            reflectiveQuadToRelative(12.5f, -15f)
            reflectiveQuadToRelative(2f, -19.5f)
            reflectiveQuadToRelative(-9f, -17f)
            reflectiveQuadToRelative(-17.5f, -9f)
            reflectiveQuadToRelative(-19.5f, 1.5f)
            reflectiveQuadToRelative(-15f, 12.5f)
            reflectiveQuadToRelative(-6f, 19f)
            quadToRelative(0f, 13f, 10f, 23f)
            reflectiveQuadToRelative(24f, 10f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
