package paufregi.connectfeed.presentation.app.gears

import androidx.compose.foundation.layout.Arrangement
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
import paufregi.connectfeed.presentation.ui.components.frame.AppFrame
import paufregi.connectfeed.presentation.ui.components.loading.AppLoading
import paufregi.connectfeed.presentation.ui.utils.add

private const val TITLE = "Gears"

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun Gears(
    user: User,
    navigate: (AppRoute) -> Unit = {},
    viewModel: GearsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GearsContent(state, user, navigate, viewModel::sync)
}

@Composable
@ExperimentalMaterial3Api
internal fun GearsContent(
    state: GearsState,
    user: User,
    navigate: (AppRoute) -> Unit = {},
    onSync: () -> Unit = {},
) {
    when (state.loading) {
        true -> AppLoading(TITLE, AppRoute.Gears)
        false -> GearList(state, user, navigate, onSync)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun GearList(
    @PreviewParameter(GearsStatePreview::class) state: GearsState,
    user: User = User.ANONYM,
    navigate: (AppRoute) -> Unit = {},
    onSync: () -> Unit = {},
) {
    AppFrame(
        title = TITLE,
        user = user,
        currentRoute = AppRoute.Gears,
        navigate = navigate,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSync,
                modifier = Modifier.testTag("sync_gears")
            ) { Icon(Icons.Default.Sync, "Sync gears") }
        }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = padding.add(horizontal = 20.dp),
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .testTag("gear_list")
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
