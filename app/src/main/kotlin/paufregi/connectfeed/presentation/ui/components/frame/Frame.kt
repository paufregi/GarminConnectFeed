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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.presentation.ui.components.loading.Loading

/**
 * Base scaffold shared by every screen. While [loading] the content is replaced by a
 * spinner and the bottom bar / FAB are hidden; callers decide how to disable top bar buttons.
 */
@Composable
@ExperimentalMaterial3Api
fun Frame(
    title: String,
    loading: Boolean = false,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        modifier = Modifier.padding(start = 20.dp).testTag("title")
                    )
                },
                navigationIcon = navigationIcon,
                actions = actions,
            )
        },
        bottomBar = { if (!loading) bottomBar() },
        floatingActionButton = { if (!loading) floatingActionButton() },
        content = { padding ->
            when (loading) {
                true -> Loading(padding)
                false -> content(padding)
            }
        }
    )
}

