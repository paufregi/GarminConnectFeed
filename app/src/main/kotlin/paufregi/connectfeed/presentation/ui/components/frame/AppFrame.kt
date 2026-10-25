package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.ui.components.NavigationDrawer

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
