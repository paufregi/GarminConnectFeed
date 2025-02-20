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

val ConnectIcons.FaceVeryHappy: ImageVector
	get() {
		if (faceVeryHappy != null) {
			return faceVeryHappy!!
		}
		faceVeryHappy = ImageVector.Builder(
            name = "Connect.FaceVeryHappy",
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
					moveTo(256f, -21f)
					quadToRelative(-37f, 0f, -72f, 12.5f)
					reflectiveQuadToRelative(-63.5f, 35.5f)
					reflectiveQuadToRelative(-47.5f, 54.5f)
					reflectiveQuadToRelative(-26f, 68f)
					reflectiveQuadToRelative(-2f, 73f)
					reflectiveQuadToRelative(22.5f, 69f)
					reflectiveQuadToRelative(44.5f, 57.5f)
					reflectiveQuadToRelative(61f, 40f)
					quadToRelative(39f, 16f, 81.5f, 16.5f)
					reflectiveQuadToRelative(82f, -15.5f)
					reflectiveQuadToRelative(69.5f, -46f)
					reflectiveQuadToRelative(46.5f, -69f)
					reflectiveQuadToRelative(17f, -81.5f)
					reflectiveQuadToRelative(-15.5f, -82f)
					reflectiveQuadToRelative(-46f, -69.5f)
					reflectiveQuadToRelative(-69.5f, -46.5f)
					reflectiveQuadToRelative(-82.5f, -16.5f)
					verticalLineToRelative(0f)
					close()
					moveTo(175f, 124f)
					quadToRelative(17f, -24f, 38f, -37f)
					reflectiveQuadToRelative(43f, -13f)
					quadToRelative(45f, 0f, 79f, 47f)
					close()
					moveTo(335f, 229f)
					quadToRelative(9f, 0f, 16f, -6f)
					reflectiveQuadToRelative(7f, -15f)
					verticalLineToRelative(-2f)
					quadToRelative(0f, -4f, 3f, -7f)
					reflectiveQuadToRelative(7.5f, -3f)
					reflectiveQuadToRelative(7f, 3f)
					reflectiveQuadToRelative(2.5f, 7f)
					quadToRelative(0f, 18f, -12.5f, 30.5f)
					reflectiveQuadToRelative(-30.5f, 12.5f)
					reflectiveQuadToRelative(-30.5f, -12.5f)
					reflectiveQuadToRelative(-12.5f, -30.5f)
					quadToRelative(0f, -4f, 2.5f, -7f)
					reflectiveQuadToRelative(6.5f, -3f)
					reflectiveQuadToRelative(7f, 2.5f)
					reflectiveQuadToRelative(4f, 6.5f)
					verticalLineToRelative(1f)
					quadToRelative(0f, 10f, 6.5f, 16.5f)
					reflectiveQuadToRelative(16.5f, 6.5f)
					verticalLineToRelative(0f)
					close()
					moveTo(176f, 229f)
					quadToRelative(9f, 0f, 15.5f, -6f)
					reflectiveQuadToRelative(7.5f, -15f)
					verticalLineToRelative(-2f)
					quadToRelative(0f, -2f, 0.5f, -4f)
					reflectiveQuadToRelative(2f, -3f)
					reflectiveQuadToRelative(3.5f, -2f)
					reflectiveQuadToRelative(4f, -1f)
					reflectiveQuadToRelative(4f, 1f)
					reflectiveQuadToRelative(3f, 2f)
					reflectiveQuadToRelative(2f, 3f)
					reflectiveQuadToRelative(1f, 4f)
					quadToRelative(0f, 18f, -13f, 30.5f)
					reflectiveQuadToRelative(-30.5f, 12.5f)
					reflectiveQuadToRelative(-30.5f, -12.5f)
					reflectiveQuadToRelative(-13f, -30.5f)
					quadToRelative(0f, -2f, 1f, -4f)
					reflectiveQuadToRelative(2.5f, -3f)
					lineToRelative(3f, -2f)
					lineToRelative(3.5f, -1f)
					lineToRelative(4f, 1f)
					lineToRelative(3.5f, 2f)
					lineToRelative(2f, 3f)
					lineToRelative(0.5f, 4f)
					quadToRelative(0f, 10f, 7f, 16.5f)
					reflectiveQuadToRelative(17f, 6.5f)
					verticalLineToRelative(0f)
					close()
				}
}
		}.build()
		return faceVeryHappy!!
	}

private var faceVeryHappy: ImageVector? = null
