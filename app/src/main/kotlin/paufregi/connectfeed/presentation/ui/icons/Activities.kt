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

val ConnectIcons.Activities: ImageVector
	get() {
		if (activities != null) {
			return activities!!
		}
		activities = ImageVector.Builder(
            name = "Connect.Activities",
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
					moveTo(493f, 373f)
					quadToRelative(-10f, 15f, -38f, 5f)
					quadToRelative(-16f, -6f, -52f, -29f)
					lineToRelative(-19f, -12f)
					quadToRelative(-38f, -26f, -60f, -36f)
					quadToRelative(-37f, -19f, -68f, -19f)
					reflectiveQuadToRelative(-72f, 21f)
					quadToRelative(-23f, 12f, -62f, 40f)
					lineToRelative(-13f, 9f)
					quadToRelative(-36f, 22f, -52f, 28f)
					quadToRelative(-28f, 10f, -37f, -5f)
					reflectiveQuadToRelative(14f, -38f)
					quadToRelative(13f, -14f, 46f, -37f)
					lineToRelative(12f, -9f)
					lineToRelative(17f, -11f)
					quadToRelative(33f, -22f, 47f, -34f)
					quadToRelative(25f, -21f, 25f, -35f)
					quadToRelative(0f, -45f, -23f, -124f)
					quadToRelative(-11f, -41f, -28f, -87f)
					lineToRelative(-3f, -8f)
					quadToRelative(-7f, -21f, -8f, -30f)
					quadToRelative(-2f, -17f, 8f, -22f)
					quadToRelative(14f, -8f, 29f, 0f)
					quadToRelative(19f, 11f, 34f, 47f)
					quadToRelative(20f, 50f, 32f, 73f)
					quadToRelative(19f, 37f, 31.5f, 37f)
					reflectiveQuadToRelative(30.5f, -34f)
					quadToRelative(11f, -20f, 31f, -68f)
					lineToRelative(4f, -8f)
					quadToRelative(16f, -36f, 35f, -47f)
					quadToRelative(15f, -8f, 27f, 0f)
					quadToRelative(10f, 6f, 8f, 22f)
					quadToRelative(-1f, 9f, -8f, 28f)
					lineToRelative(-2f, 8f)
					quadToRelative(-15f, 38f, -28f, 85f)
					quadToRelative(-24f, 82f, -24f, 128f)
					quadToRelative(0f, 17f, 34f, 43f)
					quadToRelative(18f, 14f, 54f, 36f)
					lineToRelative(1f, 1f)
					quadToRelative(34f, 18f, 56f, 37f)
					quadToRelative(31f, 28f, 21f, 45f)
					close()
					moveTo(258f, 448f)
					quadToRelative(27f, 0f, 46f, -19f)
					reflectiveQuadToRelative(19f, -45.5f)
					reflectiveQuadToRelative(-19f, -45.5f)
					reflectiveQuadToRelative(-46f, -19f)
					reflectiveQuadToRelative(-45.5f, 19f)
					reflectiveQuadToRelative(-18.5f, 45.5f)
					reflectiveQuadToRelative(18.5f, 45.5f)
					reflectiveQuadToRelative(45.5f, 19f)
					close()
				}
}
		}.build()
		return activities!!
	}

private var activities: ImageVector? = null
