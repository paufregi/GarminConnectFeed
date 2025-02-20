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

val ConnectIcons.IndoorCycling: ImageVector
	get() {
		if (indoorCycling != null) {
			return indoorCycling!!
		}
		indoorCycling = ImageVector.Builder(
            name = "Connect.IndoorCycling",
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
					moveTo(267f, 260f)
					lineToRelative(34f, -33f)
					horizontalLineToRelative(90f)
					lineToRelative(-13f, 17f)
					lineToRelative(-58f, 10f)
					lineToRelative(-40f, 58f)
					lineToRelative(-35f, 16f)
					lineToRelative(-81f, -85f)
					quadToRelative(-4f, -4f, -3.5f, -9.5f)
					reflectiveQuadToRelative(4.5f, -8.5f)
					lineToRelative(80f, -54f)
					verticalLineToRelative(-101f)
					horizontalLineToRelative(15f)
					lineToRelative(24f, 111f)
					lineToRelative(-49f, 47f)
					close()
					moveTo(424f, 25f)
					quadToRelative(27f, 15f, 38.5f, 43.5f)
					reflectiveQuadToRelative(3.5f, 57.5f)
					reflectiveQuadToRelative(-32f, 47.5f)
					reflectiveQuadToRelative(-54.5f, 18.5f)
					reflectiveQuadToRelative(-54.5f, -18.5f)
					reflectiveQuadToRelative(-32f, -48f)
					reflectiveQuadToRelative(3.5f, -57.5f)
					reflectiveQuadToRelative(38.5f, -43f)
					horizontalLineToRelative(-23f)
					lineToRelative(-11f, -23f)
					horizontalLineToRelative(157f)
					lineToRelative(-11f, 23f)
					horizontalLineToRelative(-23f)
					close()
					moveTo(323f, 141f)
					quadToRelative(12f, 17f, 30.5f, 24.5f)
					reflectiveQuadToRelative(39f, 3.5f)
					reflectiveQuadToRelative(34.5f, -18f)
					quadToRelative(20f, -20f, 19.5f, -47.5f)
					reflectiveQuadToRelative(-19.5f, -47.5f)
					reflectiveQuadToRelative(-47f, -20f)
					reflectiveQuadToRelative(-48f, 20f)
					quadToRelative(-17f, 17f, -19.5f, 41f)
					reflectiveQuadToRelative(10.5f, 44f)
					close()
					moveTo(212f, 145f)
					quadToRelative(-10f, 19f, -28f, 31.5f)
					reflectiveQuadToRelative(-39.5f, 15.5f)
					reflectiveQuadToRelative(-42f, -4.5f)
					reflectiveQuadToRelative(-35.5f, -23.5f)
					reflectiveQuadToRelative(-21f, -37f)
					reflectiveQuadToRelative(-1.5f, -42.5f)
					reflectiveQuadToRelative(18.5f, -38.5f)
					lineToRelative(-20f, -20f)
					lineToRelative(4f, -24f)
					lineToRelative(32f, 29f)
					quadToRelative(24f, -18f, 53.5f, -18f)
					reflectiveQuadToRelative(53.5f, 18f)
					lineToRelative(32f, -29f)
					lineToRelative(4f, 24f)
					lineToRelative(-20f, 20f)
					quadToRelative(17f, 21f, 19.5f, 48f)
					reflectiveQuadToRelative(-9.5f, 51f)
					close()
					moveTo(132f, 36f)
					quadToRelative(-20f, 0f, -37f, 11f)
					lineToRelative(37f, 37f)
					lineToRelative(38f, -37f)
					quadToRelative(-17f, -11f, -38f, -11f)
					close()
					moveTo(132f, 115f)
					lineToRelative(-52f, -53f)
					quadToRelative(-12f, 15f, -14f, 34.5f)
					reflectiveQuadToRelative(6.5f, 36.5f)
					reflectiveQuadToRelative(25f, 27f)
					reflectiveQuadToRelative(35.5f, 10f)
					reflectiveQuadToRelative(35.5f, -10f)
					reflectiveQuadToRelative(25f, -27f)
					reflectiveQuadToRelative(6.5f, -36.5f)
					reflectiveQuadToRelative(-14f, -34.5f)
					close()
					moveTo(331f, 322f)
					quadToRelative(8f, 6f, 12f, 15.5f)
					reflectiveQuadToRelative(2f, 19.5f)
					reflectiveQuadToRelative(-9f, 17f)
					reflectiveQuadToRelative(-17f, 9f)
					reflectiveQuadToRelative(-19.5f, -2f)
					reflectiveQuadToRelative(-15.5f, -13f)
					reflectiveQuadToRelative(-5f, -21f)
					reflectiveQuadToRelative(9.5f, -20.5f)
					reflectiveQuadToRelative(20.5f, -9.5f)
					reflectiveQuadToRelative(22f, 5f)
					close()
				}
}
		}.build()
		return indoorCycling!!
	}

private var indoorCycling: ImageVector? = null
