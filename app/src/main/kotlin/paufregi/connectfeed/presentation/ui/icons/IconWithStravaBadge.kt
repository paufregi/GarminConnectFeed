package paufregi.connectfeed.presentation.ui.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import paufregi.connectfeed.presentation.ui.icons.strava.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Strava

@Composable
fun IconWithStravaBadge(
	icon: ImageVector?,
	iconSize: Dp,
	contentDescription: String?,
	modifier: Modifier = Modifier,
	badge: Boolean = false,
	badgeSize: Dp = iconSize/2
) {
	if (icon == null) return

	Box(modifier = modifier, contentAlignment = Alignment.Center) {
		Icon(
			imageVector = icon,
			contentDescription = contentDescription,
			modifier = Modifier.size(iconSize)
		)

		if (badge) {
			Icon(
				imageVector = Icons.Strava.Logo,
				contentDescription = null,
				modifier = Modifier.align(Alignment.TopStart).alpha(0.5f).size(badgeSize).zIndex(1f),
				tint = Color.Unspecified
			)
		}
	}
}