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

val ConnectIcons.StrengthTraining: ImageVector
	get() {
		if (strengthTraining != null) {
			return strengthTraining!!
		}
		strengthTraining = ImageVector.Builder(
            name = "Connect.StrengthTraining",
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
					moveTo(459f, 171f)
					horizontalLineToRelative(-22f)
					verticalLineToRelative(21f)
					quadToRelative(0f, 4f, -3f, 7.5f)
					reflectiveQuadToRelative(-7f, 3.5f)
					horizontalLineToRelative(-22f)
					quadToRelative(-4f, 0f, -7f, -3.5f)
					reflectiveQuadToRelative(-3f, -7.5f)
					verticalLineToRelative(-21f)
					horizontalLineToRelative(-26f)
					lineToRelative(-76f, 128f)
					horizontalLineToRelative(-74f)
					lineToRelative(-75f, -128f)
					horizontalLineToRelative(-27f)
					verticalLineToRelative(21f)
					quadToRelative(0f, 4f, -3f, 7.5f)
					reflectiveQuadToRelative(-7f, 3.5f)
					horizontalLineToRelative(-22f)
					quadToRelative(-4f, 0f, -7f, -3.5f)
					reflectiveQuadToRelative(-3f, -7.5f)
					verticalLineToRelative(-21f)
					horizontalLineToRelative(-22f)
					verticalLineToRelative(-22f)
					horizontalLineToRelative(22f)
					verticalLineToRelative(-21f)
					quadToRelative(0f, -4f, 3f, -7.5f)
					reflectiveQuadToRelative(7f, -3.5f)
					horizontalLineToRelative(22f)
					quadToRelative(4f, 0f, 7f, 3.5f)
					reflectiveQuadToRelative(3f, 7.5f)
					verticalLineToRelative(21f)
					horizontalLineToRelative(86f)
					lineToRelative(-22f, -160f)
					lineToRelative(11f, -10f)
					lineToRelative(11f, 10f)
					lineToRelative(40f, 160f)
					horizontalLineToRelative(24f)
					lineToRelative(43f, -160f)
					lineToRelative(11f, -10f)
					lineToRelative(10f, 10f)
					lineToRelative(-22f, 160f)
					horizontalLineToRelative(86f)
					verticalLineToRelative(-21f)
					quadToRelative(0f, -4f, 3f, -7.5f)
					reflectiveQuadToRelative(7f, -3.5f)
					horizontalLineToRelative(22f)
					quadToRelative(4f, 0f, 7f, 3.5f)
					reflectiveQuadToRelative(3f, 7.5f)
					verticalLineToRelative(21f)
					horizontalLineToRelative(22f)
					verticalLineToRelative(22f)
					close()
					moveTo(206f, 171f)
					horizontalLineToRelative(-39f)
					lineToRelative(3f, 4f)
					quadToRelative(38f, 62f, 43f, 59f)
					quadToRelative(0f, -17f, -5f, -49f)
					close()
					moveTo(306f, 171f)
					quadToRelative(-1f, 5f, -3f, 16f)
					quadToRelative(-4f, 30f, -4f, 45f)
					quadToRelative(0f, 4f, 4f, 1f)
					quadToRelative(8f, -8f, 37f, -54f)
					lineToRelative(5f, -8f)
					horizontalLineToRelative(-39f)
					close()
					moveTo(256f, 320f)
					quadToRelative(13f, 0f, 23.5f, 7f)
					reflectiveQuadToRelative(15.5f, 19.5f)
					reflectiveQuadToRelative(2.5f, 24.5f)
					reflectiveQuadToRelative(-11.5f, 21.5f)
					reflectiveQuadToRelative(-21.5f, 12f)
					reflectiveQuadToRelative(-24.5f, -2.5f)
					reflectiveQuadToRelative(-19.5f, -16f)
					reflectiveQuadToRelative(-7.5f, -23f)
					quadToRelative(0f, -18f, 13f, -30.5f)
					reflectiveQuadToRelative(30f, -12.5f)
					close()
				}
}
		}.build()
		return strengthTraining!!
	}

private var strengthTraining: ImageVector? = null
