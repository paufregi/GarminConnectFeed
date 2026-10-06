package paufregi.connectfeed.presentation.app.gears

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.ui.components.cards.GearCard
import paufregi.connectfeed.presentation.ui.components.screens.MenuScreen
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

    MenuScreen(
        user = user,
        isLoading = state.loading,
        currentRoute = AppRoute.Gears,
        navigate = navigate,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.sync() },
                modifier = Modifier.testTag("sync_gears")
            ) { Icon(Icons.Default.Sync, "Sync gears") }
        }
    ) { GearsContent(state, it) }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun GearsContent(
    @PreviewParameter(GearsStatePreview::class) state: GearsState,
    padding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = padding.add(horizontal = 20.dp),
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(padding)
            .testTag("gears_list")
    ) {
        if (state.gears.isEmpty()) {
            item { Text("No gears", modifier = Modifier.testTag("no_gears")) }
        } else {
            items(state.gears, key = { it.id }) { gear ->
                GearCard(
                    gear = gear,
                    modifier = Modifier.fillMaxWidth().testTag("gear_${gear.id}")
                )
            }
        }
    }
}
