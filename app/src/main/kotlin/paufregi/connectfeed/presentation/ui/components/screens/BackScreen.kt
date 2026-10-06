package paufregi.connectfeed.presentation.ui.components.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Loading

@Composable
@ExperimentalMaterial3Api
fun BackScreen(
    title: String? = null,
    isLoading: Boolean = false,
    navigateBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    bottomBarContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { title?.let{ Text(it, modifier = Modifier.padding(start = 20.dp).testTag("title")) } },
                navigationIcon = {
                    Button(
                        modifier = Modifier.testTag("back"),
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        enabled = !isLoading,
                        onClick = navigateBack,
                    )
                },
                actions = actions,
            )
        },
        bottomBar = { if (!isLoading) { BottomAppBar {  bottomBarContent() } } },
        content = { padding ->
            when (isLoading) {
                true -> Loading(padding)
                false -> content(padding)
            }
        }
    )
}

object BackScreen {
    @Composable
    fun Navigate(
        icon: ImageVector,
        enabled: Boolean = true,
        onClick: () -> Unit = {}
    ) {
        Button(
            icon = icon,
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.testTag("navigate"),
        )
    }
}
