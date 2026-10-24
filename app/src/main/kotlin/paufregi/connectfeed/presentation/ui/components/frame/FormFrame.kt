package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Frame for edit flows: back navigation plus an optional single action in the top bar.
 * Top bar buttons are disabled while [loading] or when [enabled] is false (e.g. on result screens).
 */
@Composable
@ExperimentalMaterial3Api
fun FormFrame(
    title: String,
    navBack: () -> Unit = {},
    action: ImageVector? = null,
    onAction: () -> Unit = {},
    enabled: Boolean = true,
    loading: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {
    val buttonsEnabled = enabled && !loading

    Frame(
        title = title,
        loading = loading,
        navigationIcon = { BackButton(enabled = buttonsEnabled, onClick = navBack) },
        actions = {
            action?.let { ActionButton(icon = it, enabled = buttonsEnabled, onClick = onAction) }
        },
        bottomBar = bottomBar,
        content = content,
    )
}
