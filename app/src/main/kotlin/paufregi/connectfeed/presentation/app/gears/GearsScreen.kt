package paufregi.connectfeed.presentation.app.gears

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.ui.components.GearCard
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.models.ProcState

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun GearsScreen(
    padding: PaddingValues = PaddingValues(),
    viewModel: GearsViewModel = hiltViewModel()
) {
    val viewModel = hiltViewModel<GearsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val p = state.process) {
        is ProcState.Running -> Loading()
        is ProcState.Success -> successInfo { viewModel.onAction(GearsAction.Reset) }(p.message ?: "Sync completed")
        is ProcState.Failure -> failureInfo { viewModel.onAction(GearsAction.Reset) }(p.reason)
        else -> GearsContent(state, padding, viewModel::onAction)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun GearsContent(
    @PreviewParameter(GearsStatePreview::class) state: GearsState,
    padding: PaddingValues = PaddingValues(),
    onAction: (GearsAction) -> Unit = {},
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(GearsAction.Sync) },
                modifier = Modifier.testTag("sync_gears")
            ) { Icon(Icons.Default.Sync, "Sync gears") }
        },
        modifier = Modifier.fillMaxSize().testTag("gears_content")
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                    start = padding.calculateLeftPadding(LayoutDirection.Ltr) + 20.dp,
                    end = padding.calculateRightPadding(LayoutDirection.Ltr) + 20.dp,
                )
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
}
