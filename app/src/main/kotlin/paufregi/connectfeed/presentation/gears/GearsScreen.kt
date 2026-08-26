package paufregi.connectfeed.presentation.gears

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
internal fun GearsScreen(
    nav: NavHostController = rememberNavController(),
) {
    val viewModel = hiltViewModel<GearsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val process = state.process) {
        is ProcessState.Processing -> SimpleScaffold { Loading(it) }
        is ProcessState.Success -> SimpleScaffold {
            successInfo { viewModel.onAction(GearsAction.Reset) }(process, it)
        }
        is ProcessState.Failure -> SimpleScaffold {
            failureInfo { viewModel.onAction(GearsAction.Reset) }(process, it)
        }
        is ProcessState.Idle -> NavigationScaffold(
            topItems = Navigation.topItems,
            bottomItems = Navigation.bottomItems,
            selectedIndex = Navigation.GEARS,
            nav = nav,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.onAction(GearsAction.Sync) },
                    modifier = Modifier.testTag("sync_gears")
                ) { Icon(Icons.Default.Sync, "Sync gears") }
            }
        ) {
            GearsContent(state = state, paddingValues = it)
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun GearsContent(
    @PreviewParameter(GearsStatePreview::class) state: GearsState,
    paddingValues: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr) + 20.dp,
                end = paddingValues.calculateRightPadding(LayoutDirection.Ltr) + 20.dp,
            )
            .testTag("gears_content")
    ) {
        if (state.gears.isEmpty()) {
            item { Text("No gears") }
        } else {
            items(state.gears, key = { it.id }) { gear ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = gear.name,
                            modifier = Modifier
                                .weight(1f)
                                .basicMarquee(),
                            maxLines = 1,
                        )
                        Text(text = gear.distanceLabel)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

private val Gear.distanceLabel: String
    get() = distance?.let { "${Formatter.distance(it.toDouble())} km" } ?: "-"

