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

val ConnectIcons.Soccer: ImageVector
	get() {
		if (soccer != null) {
			return soccer!!
		}
		soccer = ImageVector.Builder(
            name = "Connect.Soccer",
            defaultWidth = 522.dp,
            defaultHeight = 512.dp,
            viewportWidth = 522f,
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
					moveTo(256f, 0f)
					quadToRelative(52f, 0f, 96f, 26f)
					reflectiveQuadToRelative(70f, 70f)
					reflectiveQuadToRelative(26f, 96f)
					reflectiveQuadToRelative(-26f, 96f)
					reflectiveQuadToRelative(-70f, 70f)
					reflectiveQuadToRelative(-96f, 26f)
					reflectiveQuadToRelative(-96f, -26f)
					reflectiveQuadToRelative(-70f, -70f)
					reflectiveQuadToRelative(-26f, -96f)
					reflectiveQuadToRelative(26f, -96f)
					reflectiveQuadToRelative(70f, -70f)
					reflectiveQuadToRelative(96f, -26f)
					close()
					moveTo(261f, 71f)
					lineToRelative(-22f, -49f)
					quadToRelative(-54f, 6f, -94f, 41f)
					quadToRelative(-5f, 15f, -7f, 35f)
					lineToRelative(-1f, 16f)
					lineToRelative(67f, 18f)
					lineToRelative(14f, -17f)
					quadToRelative(18f, -21f, 43f, -44f)
					close()
					moveTo(101f, 158f)
					lineToRelative(-12f, 1f)
					quadToRelative(-3f, 16f, -3f, 33f)
					quadToRelative(0f, 47f, 24f, 88f)
					quadToRelative(3f, -13f, 15f, -39f)
					lineToRelative(2f, -6f)
					lineToRelative(-11f, -25f)
					quadToRelative(-13f, -30f, -15f, -52f)
					close()
					moveTo(215f, 335f)
					lineToRelative(-10f, 4f)
					quadToRelative(-13f, 4f, -24f, 6f)
					quadToRelative(33f, 16f, 71f, 17f)
					quadToRelative(-12f, -7f, -26f, -18f)
					close()
					moveTo(353f, 332f)
					quadToRelative(42f, -29f, 61f, -76f)
					lineToRelative(-14f, -6f)
					quadToRelative(-15f, 21f, -37f, 42f)
					lineToRelative(-19f, 17f)
					close()
					moveTo(224f, 172f)
					lineToRelative(-57f, 63f)
					verticalLineToRelative(0f)
					quadToRelative(16f, 31f, 37f, 56f)
					quadToRelative(11f, 12f, 19f, 18f)
					verticalLineToRelative(0f)
					quadToRelative(24f, -4f, 45f, -11f)
					quadToRelative(15f, -4f, 27f, -9f)
					lineToRelative(8f, -5f)
					verticalLineToRelative(-84f)
					quadToRelative(-22f, -5f, -42f, -12f)
					quadToRelative(-15f, -5f, -27f, -11f)
					close()
					moveTo(330f, 94f)
					lineToRelative(14f, 80f)
					lineToRelative(63f, 28f)
					lineToRelative(19f, -24f)
					verticalLineToRelative(-2f)
					quadToRelative(-3f, -31f, -16.5f, -58.5f)
					reflectiveQuadToRelative(-35.5f, -48.5f)
					close()
				}
}
		}.build()
		return soccer!!
	}

private var soccer: ImageVector? = null
