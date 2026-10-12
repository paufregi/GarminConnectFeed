package paufregi.connectfeed.presentation.ui.components.loading

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import paufregi.connectfeed.presentation.ui.components.frame.FormFrame
import paufregi.connectfeed.presentation.ui.components.frame.TopButton

@Composable
@ExperimentalMaterial3Api
fun FormLoading(
    title: String,
    actionIcon: ImageVector? = null
) {
    FormFrame(
        title = title,
        navIcon = TopButton.back(false),
        actionIcon = actionIcon?.let { TopButton.action(it, enabled = false) } ?: {},
    ) { Loading(it) }
}