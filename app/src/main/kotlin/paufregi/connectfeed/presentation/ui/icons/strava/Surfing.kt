package paufregi.connectfeed.presentation.ui.icons.strava

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StravaIcons.Surfing: ImageVector
    get() = image ?: ImageVector.Builder(
        name = "Strava.Surfing",
        defaultWidth = 24.dp,
        defaultHeight = 23.dp,
        viewportWidth = 24f,
        viewportHeight = 23f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(21.78f, 0.711f)
            curveToRelative(-0.243f, -0.242f, -0.562f, -0.394f, -0.853f, -0.49f)
            arcToRelative(4.483f, 4.483f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.083f, -0.204f)
            curveToRelative(-0.817f, -0.06f, -1.857f, 0.037f, -3.085f, 0.398f)
            curveToRelative(-2.458f, 0.723f, -5.698f, 2.514f, -9.506f, 6.322f)
            curveTo(1.471f, 12.52f, 0.199f, 16.981f, 0.032f, 17.667f)
            curveToRelative(-0.095f, 0.386f, 0.03f, 0.775f, 0.29f, 1.036f)
            lineTo(3.791f, 22.169f)
            curveToRelative(0.26f, 0.26f, 0.65f, 0.384f, 1.035f, 0.29f)
            curveToRelative(0.482f, -0.118f, 2.84f, -0.784f, 6.215f, -3.19f)
            curveToRelative(1.41f, 2.056f, 4.444f, 2.357f, 6.22f, 0.448f)
            lineToRelative(0.06f, -0.066f)
            curveToRelative(1.485f, 1.726f, 4.246f, 1.887f, 5.903f, 0.105f)
            lineToRelative(-1.464f, -1.362f)
            arcToRelative(1.964f, 1.964f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.219f, -0.51f)
            lineToRelative(-0.142f, -0.306f)
            arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.64f, -0.26f)
            lineToRelative(-0.963f, 1.037f)
            arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.16f, -0.3f)
            arcToRelative(39.8f, 39.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.12f, -2.817f)
            curveToRelative(0.284f, -0.284f, 0.556f, -0.564f, 0.818f, -0.841f)
            lineToRelative(0.131f, 0.282f)
            curveToRelative(1.063f, 2.284f, 3.87f, 2.965f, 5.843f, 1.646f)
            lineToRelative(0.831f, -0.556f)
            lineToRelative(-1.111f, -1.662f)
            lineToRelative(-0.832f, 0.555f)
            arcToRelative(2.002f, 2.002f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.917f, -0.826f)
            lineToRelative(-0.497f, -1.069f)
            curveToRelative(2.29f, -2.749f, 3.495f, -5.128f, 4.056f, -7.036f)
            curveToRelative(0.36f, -1.227f, 0.457f, -2.267f, 0.397f, -3.085f)
            curveToRelative(-0.03f, -0.407f, -0.1f, -0.772f, -0.203f, -1.082f)
            curveToRelative(-0.097f, -0.291f, -0.248f, -0.61f, -0.491f, -0.853f)
            close()
            moveTo(8.668f, 8.151f)
            curveToRelative(3.634f, -3.635f, 6.596f, -5.21f, 8.656f, -5.817f)
            curveToRelative(1.032f, -0.303f, 1.83f, -0.362f, 2.375f, -0.322f)
            arcToRelative(2.512f, 2.512f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.654f, 0.127f)
            lineToRelative(0.021f, 0.058f)
            curveToRelative(0.042f, 0.126f, 0.086f, 0.323f, 0.106f, 0.596f)
            curveToRelative(0.04f, 0.545f, -0.019f, 1.343f, -0.322f, 2.375f)
            curveToRelative(-0.606f, 2.06f, -2.182f, 5.022f, -5.817f, 8.656f)
            curveToRelative(-4.597f, 4.597f, -8.234f, 6.123f, -9.517f, 6.552f)
            lineToRelative(-0.647f, -0.647f)
            lineToRelative(12.97f, -12.97f)
            lineToRelative(-1.413f, -1.414f)
            lineToRelative(-12.97f, 12.97f)
            lineToRelative(-0.648f, -0.647f)
            curveToRelative(0.429f, -1.283f, 1.955f, -4.92f, 6.552f, -9.517f)
            close()
        }
    }.build().also { image = it }

private var image: ImageVector? = null
