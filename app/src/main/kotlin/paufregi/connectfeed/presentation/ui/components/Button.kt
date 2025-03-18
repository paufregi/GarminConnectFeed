package paufregi.connectfeed.presentation.ui.components

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.material3.IconButton as MaterialIconButton

@Composable
fun Button(
    text: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    MaterialButton(
        modifier = modifier,
        colors = colors,
        onClick = onClick,
        enabled = enabled
    ) {
        Text(text)
    }
}

@Composable
fun Button(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    description: String? = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    MaterialIconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(icon, description)
    }
}
