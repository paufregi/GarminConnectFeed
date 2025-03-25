package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class IconRadioItem<T>(
    val value: T,
    val icon: ImageVector
)

@Composable
fun <T> IconRadioGroup(
    options: List<IconRadioItem<T>>,
    selected: T? = null,
    onClick: (T?) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            val isSelected = selected == option.value
            Box(
                modifier = Modifier
                    .size(if (isSelected) 50.dp else 42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .alpha(if (isSelected) 1f else 0.5f)
                    .clickable { onClick(if (isSelected) null else option.value) }
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(top = if (isSelected) 7.dp else 6.dp)
                        .align(Alignment.Center)
                        .size(if (isSelected) 46.dp else 38.dp)
                        .alpha(if (isSelected) 1f else 0.5f)
                )
            }
        }
    }
}
