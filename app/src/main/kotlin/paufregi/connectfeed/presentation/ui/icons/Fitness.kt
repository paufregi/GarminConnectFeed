package paufregi.connectfeed.presentation.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Fitness: ImageVector
    get() {
        if (image != null) {
            return image!!
        }
        image = ImageVector.Builder(
            name = "Connect.Fitness",
            defaultWidth = 522.dp,
            defaultHeight = 512.dp,
            viewportWidth = 522f,
            viewportHeight = 512f
        ).apply {
                path(
                    fill = SolidColor(Color.Black),
                ) {
                    moveTo(221f, 332f)
                    lineTo(169f, 306f)
                    lineTo(158f, 364f)
                    lineTo(105f, 456f)
                    lineTo(118f, 468f)
                    lineTo(191f, 382f)
                    close()
                    moveTo(407f, 427f)
                    lineTo(296f, 427f)
                    lineTo(310f, 309f)
                    lineTo(243f, 256f)
                    lineTo(264f, 191f)
                    lineTo(316f, 220f)
                    lineTo(372f, 137f)
                    lineTo(351f, 137f)
                    lineTo(310f, 173f)
                    lineTo(265f, 148f)
                    lineTo(200f, 138f)
                    lineTo(130f, 174f)
                    lineTo(118f, 257f)
                    lineTo(135f, 266f)
                    lineTo(154f, 197f)
                    lineTo(197f, 185f)
                    lineTo(181f, 249f)
                    curveTo(179f, 259f, 178f, 266f, 178f, 270f)
                    curveTo(178.66797f, 276f, 181f, 280f, 185f, 282f)
                    lineTo(276f, 331f)
                    lineTo(276f, 422f)
                    lineTo(288f, 425f)
                    lineTo(178f, 425f)
                    lineTo(174f, 427f)
                    curveTo(171.33203f, 428.33203f, 169.33203f, 429.83203f, 168f, 431.5f)
                    curveTo(166.66797f, 433.16797f, 164.66797f, 436.66797f, 162f, 442f)
                    curveTo(159.33203f, 450.66797f, 158f, 459.66797f, 158f, 469f)
                    lineTo(179f, 469f)
                    lineTo(185f, 458f)
                    lineTo(399f, 458f)
                    lineTo(405f, 469f)
                    lineTo(427f, 469f)
                    curveTo(425f, 459f, 423f, 451.33203f, 421f, 446f)
                    curveTo(417.66797f, 437.33203f, 413f, 431f, 407f, 427f)
                    close()
                    moveTo(247f, 118f)
                    curveTo(259f, 118f, 268.66797f, 113.33203f, 276f, 104f)
                    curveTo(283.33203f, 94.66797f, 286f, 84.33203f, 284f, 73f)
                    curveTo(282.66797f, 65.66797f, 279.33203f, 59.33203f, 274f, 54f)
                    curveTo(268.66797f, 48.66797f, 262.16797f, 45.16797f, 254.5f, 43.5f)
                    curveTo(246.83203f, 41.83203f, 239.66797f, 42.5f, 233f, 45.5f)
                    curveTo(226.33203f, 48.5f, 220.83203f, 53.16797f, 216.5f, 59.5f)
                    curveTo(212.16797f, 65.83203f, 210f, 72.66797f, 210f, 80f)
                    curveTo(210f, 90f, 213.66797f, 98.83203f, 221f, 106.5f)
                    curveTo(228.33203f, 114.16797f, 237f, 118f, 247f, 118f)
                    close()
                    moveTo(247f, 118f)
            }
        }.build()
        return image!!
    }

private var image: ImageVector? = null
