package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TextIcon(
    icon: ImageVector,
    text: String,
    tint: Boolean? = null,
) {
    Icon(icon, text.lowercase(), Modifier.size(24.dp).padding(end = 5.dp), if (tint == true) Color.Unspecified else Color.LightGray )
    Text(text)
}
