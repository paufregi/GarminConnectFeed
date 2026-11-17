package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute

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

@Composable
@ExperimentalMaterial3Api
fun AppFrame(
    title: String,
    currentRoute: AppRoute,
    user: User? = null,
    navigate: (AppRoute) -> Unit = {},
    loading: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    NavigationDrawer(
        navigate = navigate,
        currentRoute = currentRoute,
    ) { drawerState ->
        Frame(
            title = title,
            loading = loading,
            navigationIcon = {
                MenuButton(
                    enabled = !loading,
                    onClick = { scope.launch { drawerState.open() } },
                )
            },
            actions = { UserAvatar(user) },
            floatingActionButton = floatingActionButton,
            content = content,
        )
    }
}

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

@Composable
@ExperimentalMaterial3Api
fun ActivityFrame(
    title: String,
    user: User? = null,
    loading: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) = Frame(
    title = title,
    loading = loading,
    actions = { UserAvatar(user) },
    bottomBar = bottomBar,
    content = content,
)