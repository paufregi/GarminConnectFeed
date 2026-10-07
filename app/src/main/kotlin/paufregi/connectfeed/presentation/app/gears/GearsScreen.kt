package paufregi.connectfeed.presentation.app.gears

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.NavigationDrawer
import paufregi.connectfeed.presentation.ui.components.cards.GearCard
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun Gears(
    user: User,
    navigate: (AppRoute) -> Unit = {},
    viewModel: GearsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.loading){
        true -> Loading(user)
        false -> List(
            state = state,
            user = user,
            navigate = navigate,
            onSync = viewModel::sync
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun Frame(
    user: User,
    navigate: (AppRoute) -> Unit = {},
    enableMenu: Boolean = true,
    onSync: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val scope = rememberCoroutineScope()

    NavigationDrawer(
        navigate = navigate,
        currentRoute = AppRoute.Gears,
    ) { drawerState ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gears", modifier = Modifier.padding(start = 20.dp).testTag("title")) },
                    navigationIcon = {
                        Button(
                            modifier = Modifier.testTag("menu"),
                            icon = Icons.Filled.Menu,
                            enabled = enableMenu,
                            onClick = { scope.launch { drawerState.open() } },
                        )
                    },
                    actions = {
                        AsyncImage(
                            model = user.profileImageUrl.takeIf { it.isNotEmpty() } ?: Icons.Default.Person,
                            contentDescription = user.name,
                            modifier = Modifier
                                .padding(end = 14.dp)
                                .size(46.dp)
                                .clip(CircleShape)
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onSync,
                    modifier = Modifier.testTag("sync_gears")
                ) { Icon(Icons.Default.Sync, "Sync gears") }
            },
            content = content
        )
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun List(
    @PreviewParameter(GearsStatePreview::class) state: GearsState,
    user: User = User.ANONYM,
    navigate: (AppRoute) -> Unit = {},
    onSync: () -> Unit = {},
) {
    Frame(
        user = user,
        navigate = navigate,
        onSync = onSync,
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = padding.add(horizontal = 20.dp),
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .testTag("activity_list")
        ) {
            when (state.gears.isEmpty()) {
                true -> item { Text("No gears", modifier = Modifier.testTag("no_gears")) }
                false -> items(state.gears, key = { it.id }) { gear ->
                    GearCard(
                        gear = gear,
                        modifier = Modifier.fillMaxWidth().testTag("gear_${gear.id}")
                    )
                }
            }
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun Loading(
    user: User = User.ANONYM,
) {
    Frame(
        user = user,
        enableMenu = false,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(horizontal = 20.dp))
                .testTag("gears_loading")
        ) {
            CircularProgressIndicator(
                strokeWidth = 8.dp,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}
