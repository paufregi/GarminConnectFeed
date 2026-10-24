package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.NavigationDrawer

@Composable
@ExperimentalMaterial3Api
fun AppFrame(
    title: String,
    currentRoute: AppRoute,
    user: User? = null,
    navigate: (AppRoute) -> Unit = {},
    enableMenu: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val scope = rememberCoroutineScope()

    NavigationDrawer(
        navigate = navigate,
        currentRoute = currentRoute,
    ) { drawerState ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            title,
                            modifier = Modifier.padding(start = 20.dp).testTag("title")
                        )
                    },
                    navigationIcon = {
                        Button(
                            modifier = Modifier.testTag("menu"),
                            icon = Icons.Filled.Menu,
                            onClick = { scope.launch { drawerState.open() } },
                            enabled = enableMenu
                        )
                    },
                    actions = {
                        user?.let {
                            AsyncImage(
                                model = user.profileImageUrl.takeIf { it.isNotEmpty() },
                                contentDescription = user.name,
                                fallback = rememberVectorPainter(Icons.Default.Person),
                                modifier = Modifier
                                    .padding(end = 14.dp)
                                    .size(46.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                )
            },
            floatingActionButton = floatingActionButton,
            content = content
        )
    }
}
