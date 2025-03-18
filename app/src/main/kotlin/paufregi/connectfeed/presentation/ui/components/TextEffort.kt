package paufregi.connectfeed.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextEffort(
    effort: Float,
    modifier: Modifier = Modifier
) {
    val score = (effort / 10).toInt()
    val label = when (score) {
        0 -> "None selected"
        1 -> "Very light"
        2 -> "Light"
        3 -> "Moderate"
        4 -> "Somewhat Hard"
        5, 6 -> "Hard"
        7, 8 -> "Very Hard"
        9 -> "Extremely Hard"
        10 -> "Maximum"
        else -> "What!?"
    }
    Text("$score - $label", modifier = modifier)
}
