package paufregi.connectfeed.presentation.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ConnectIcons.Meditation: ImageVector
	get() {
		if (meditation != null) {
			return meditation!!
		}
		meditation = ImageVector.Builder(
            name = "Connect.Meditation",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
			group(
				scaleX = 1f,
				scaleY = -1f,
				translationX = 0f,
				translationY = 409f,
				pivotX = 0f,
				pivotY = 0f,
) {
				path(
    				fill = SolidColor(Color(0xFF000000)),
    				fillAlpha = 1.0f,
    				stroke = null,
    				strokeAlpha = 1.0f,
    				strokeLineWidth = 1.0f,
    				strokeLineCap = StrokeCap.Butt,
    				strokeLineJoin = StrokeJoin.Miter,
    				strokeLineMiter = 1.0f,
    				pathFillType = PathFillType.NonZero
				) {
					moveTo(186f, 272f)
					quadToRelative(-5f, -8f, -8f, -27f)
					quadToRelative(-2f, -13f, -2f, -27f)
					quadToRelative(-11f, 12f, -26f, 20f)
					quadToRelative(-10f, 6f, -18f, 9f)
					quadToRelative(1f, 16f, 5f, 30f)
					quadToRelative(3f, 10f, 7f, 16f)
					quadToRelative(20f, -5f, 42f, -21f)
					close()
					moveTo(382f, 246f)
					quadToRelative(-10f, -2f, -25f, -12f)
					quadToRelative(-12f, -7f, -22f, -15f)
					quadToRelative(1f, 16f, -1f, 33f)
					quadToRelative(-2f, 12f, -5f, 20f)
					quadToRelative(12f, 10f, 25f, 15f)
					quadToRelative(10f, 4f, 17f, 5f)
					quadToRelative(9f, -19f, 11f, -46f)
					close()
					moveTo(255f, 64f)
					quadToRelative(-24f, 24f, -39f, 60f)
					quadToRelative(-18f, 45f, -12f, 90f)
					quadToRelative(7f, 56f, 51f, 106f)
					quadToRelative(44f, -49f, 51f, -105f)
					quadToRelative(6f, -45f, -12f, -90f)
					quadToRelative(-15f, -36f, -39f, -61f)
					close()
					moveTo(295f, 65f)
					quadToRelative(32f, 3f, 62f, 19f)
					quadToRelative(36f, 20f, 59f, 53f)
					quadToRelative(27f, 39f, 32f, 94f)
					quadToRelative(-68f, 0f, -113f, -40f)
					quadToRelative(1f, -35f, -12f, -70f)
					quadToRelative(-10f, -30f, -28f, -56f)
					close()
					moveTo(218f, 64f)
					quadToRelative(-32f, 3f, -63f, 20f)
					quadToRelative(-38f, 19f, -61f, 53f)
					quadToRelative(-27f, 40f, -30f, 96f)
					quadToRelative(66f, -3f, 108f, -41f)
					quadToRelative(0f, -33f, 15f, -71f)
					quadToRelative(11f, -29f, 31f, -57f)
					close()
				}
}
		}.build()
		return meditation!!
	}

private var meditation: ImageVector? = null
