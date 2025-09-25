package paufregi.connectfeed.presentation.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import paufregi.connectfeed.R
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
        Image(
            painter = rememberAsyncImagePainter(R.mipmap.ic_launcher),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .testTag("app_icon")
        )
        Spacer(modifier = Modifier.size(42.dp))
        Text(text = "Version: ${state.currentVersion?: "Unknown"}")
        if(state.hasUpdate) {
            Text(text = "Update available: ${state.latestRelease?.version}")
        }
        if(state.process is ProcessState.Failure) {
            Text(text = state.process.reason, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.size(8.dp))
        Button(
            text = "Check for updates",
            onClick = { onAction(InfoAction.CheckUpdate) },
            enabled = state.process != ProcessState.Processing
        )
        if(state.hasUpdate) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                text = "Update",
                onClick = { onAction(InfoAction.Update) },
                enabled = state.process != ProcessState.Processing
            )
        }
        if(state.downloadProgress != null) {
            Text(text = "Download progress: ${state.downloadProgress}%")
        }
    }
}
