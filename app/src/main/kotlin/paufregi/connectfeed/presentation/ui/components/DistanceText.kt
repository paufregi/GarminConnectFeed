package paufregi.connectfeed.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun DistanceText(
    distance: String?
) {
    if (distance != null) {
        Text(text = "$distance km", fontSize = 11.sp)
    }
}