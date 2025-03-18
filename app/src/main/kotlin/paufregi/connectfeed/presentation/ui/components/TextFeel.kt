package paufregi.connectfeed.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextFeel(
    feel: Float?,
    modifier: Modifier = Modifier
) {
    val label = when (feel) {
        0f -> "Very weak"
        25f -> "Weak"
        50f -> "Normal"
        75f -> "Strong"
        100f -> "Very strong"
        null -> "None selected"
        else -> "What!?"
    }
    Text(label, modifier = modifier)
}
