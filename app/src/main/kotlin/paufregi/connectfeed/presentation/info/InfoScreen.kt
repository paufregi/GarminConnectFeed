package paufregi.connectfeed.presentation.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
internal fun InfoScreen(nav: NavHostController = rememberNavController()) {
    val viewModel = hiltViewModel<InfoViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationScaffold(
        items = Navigation.items,
        selectedIndex = Navigation.INFO,
        nav = nav,
    ) {
        InfoContent(state, viewModel::onAction, it)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun InfoContent(
    @PreviewParameter(InfoStatePreview::class) state: InfoState,
    onAction: (InfoAction) -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
            .testTag("info_screen")
    ) {
        Text(text = "Version: ${state.currentVersion?: "Unknown"}")
        Spacer(modifier = Modifier.size(24.dp))
        if (state.process == ProcessState.Processing) {
            CircularProgressIndicator(Modifier.size(16.dp).testTag("loading"))
        }
        if (state.currentVersion != null && state.latestRelease != null) {
            if (state.currentVersion.lessThan(state.latestRelease.version)) {
                Text(text = "Update available: ${state.latestRelease.version}")
                Spacer(modifier = Modifier.size(8.dp))
                Button(text = "Update", onClick = { onAction(InfoAction.Update) })
            }
        }
    }
}
