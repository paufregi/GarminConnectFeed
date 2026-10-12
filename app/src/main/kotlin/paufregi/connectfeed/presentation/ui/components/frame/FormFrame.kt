package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterial3Api
fun FormFrame(
    title: String,
    navIcon: @Composable () -> Unit = {},
    actionIcon: @Composable RowScope.() -> Unit = {},
    bottomContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                },
                navigationIcon = navIcon,
                actions = actionIcon,
            )
        },
        bottomBar = bottomContent,
        content = content,
    )
}
