package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Logo: ImageVector
    get() {
        if (logo != null) {
            return logo!!
        }
        logo = ImageVector.Builder(
            name = "Strava.Logo",
            defaultWidth = 412.dp,
            defaultHeight = 596.dp,
            viewportWidth = 412f,
            viewportHeight = 596f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFC4C02)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(169.107f, 0.117188f)
                lineTo(0.616943f, 343.117f)
                horizontalLineTo(100.929f)
                lineTo(169.107f, 204.26f)
                lineTo(237.332f, 343.117f)
                horizontalLineTo(337.617f)
                lineTo(169.107f, 0.117188f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFC4C02)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(287.486f, 595.883f)
                lineTo(411.383f, 343.117f)
                lineTo(337.62f, 343.117f)
                lineTo(287.486f, 445.444f)
                lineTo(237.317f, 343.117f)
                lineTo(163.574f, 343.117f)
                lineTo(287.486f, 595.883f)
                close()
            }
        }.build()
        return logo!!
    }

private var logo: ImageVector? = null
