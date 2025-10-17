package paufregi.connectfeed.presentation.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Logo: ImageVector
    get() {
        if (logo != null) {
            return logo!!
        }
        logo = ImageVector.Builder(
            name = "Connect.Logo",
            defaultWidth = 1501.dp,
            defaultHeight = 1335.dp,
            viewportWidth = 1501f,
            viewportHeight = 1335f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF007CC2))
            ) {
                moveToRelative(833.3f, 30.9f)
                curveToRelative(14.6f, 12.6f, 25.6f, 30.2f, 108f, 173.7f)
                curveToRelative(4.5f, 7.5f, 14.9f, 25.7f, 23.6f, 40.6f)
                curveToRelative(8.6f, 14.6f, 18.5f, 31.6f, 21.4f, 37.3f)
                curveToRelative(3.3f, 5.7f, 59.1f, 102.4f, 124.2f, 214.9f)
                curveToRelative(64.7f, 112.5f, 120.2f, 208.3f, 122.9f, 213.4f)
                curveToRelative(3f, 4.7f, 25.4f, 44.1f, 50.2f, 87.1f)
                curveToRelative(25f, 43f, 47.7f, 82.7f, 51f, 88.1f)
                curveToRelative(3f, 5.3f, 15.2f, 26.5f, 27.1f, 47.1f)
                curveToRelative(20.6f, 35.5f, 32.9f, 57f, 40f, 70.1f)
                curveToRelative(1.8f, 3.3f, 4.8f, 8.1f, 6.6f, 10.5f)
                curveToRelative(2.1f, 2.4f, 7.2f, 11f, 11.6f, 19.4f)
                curveToRelative(4.2f, 8f, 10.8f, 19.7f, 14.1f, 25.4f)
                curveToRelative(60.8f, 104.4f, 64.4f, 112.2f, 66.5f, 143.5f)
                curveToRelative(3.6f, 59.4f, -34.9f, 111.9f, -94f, 128.3f)
                curveToRelative(-18.8f, 5.1f, -1273.9f, 6f, -1304.4f, 0.6f)
                curveToRelative(-70.1f, -11.6f, -115.1f, -82f, -98.1f, -152.8f)
                curveToRelative(2.4f, -9.8f, 19.4f, -42.9f, 46.5f, -90.4f)
                curveToRelative(23.6f, -40.9f, 43f, -75.8f, 43f, -77.3f)
                curveToRelative(0f, -1.5f, 0.9f, -2.7f, 2.4f, -2.7f)
                curveToRelative(1.2f, 0f, 3.6f, -3f, 5.4f, -6.8f)
                curveToRelative(1.7f, -3.6f, 10.4f, -19.4f, 19.3f, -35f)
                curveToRelative(9.3f, -15.5f, 20f, -34.3f, 24.2f, -41.7f)
                curveToRelative(4.2f, -7.5f, 16.1f, -28.4f, 26.9f, -46.6f)
                curveToRelative(10.7f, -18.2f, 20.3f, -34.9f, 21.2f, -37.3f)
                curveToRelative(1.2f, -2.4f, 3.8f, -6.9f, 5.6f, -10.2f)
                curveToRelative(10.8f, -16.7f, 23.3f, -37.6f, 23.3f, -38.4f)
                curveToRelative(0f, -0.9f, 5.7f, -11.1f, 12.8f, -23.3f)
                curveToRelative(7.2f, -12f, 16.5f, -27.8f, 20.6f, -35.2f)
                curveToRelative(4.2f, -7.5f, 17f, -29.6f, 28.4f, -49.3f)
                curveToRelative(21.5f, -36.7f, 198.4f, -343.8f, 208.9f, -362.6f)
                curveToRelative(12.8f, -22.4f, 78.8f, -136.1f, 81.4f, -140.3f)
                curveToRelative(1.5f, -2.3f, 5.4f, -9.2f, 8.4f, -14.9f)
                curveToRelative(3.3f, -5.6f, 15.5f, -27.1f, 27.4f, -47.7f)
                curveToRelative(12.3f, -20.6f, 23.3f, -40f, 25.1f, -43.3f)
                curveToRelative(21.8f, -39.4f, 48.1f, -61.2f, 85.7f, -71.3f)
                curveToRelative(37f, -9.9f, 82.3f, 0.9f, 112.8f, 27.1f)
                close()
            }
        }.build()
        return logo!!
    }

private var logo: ImageVector? = null
