package paufregi.connectfeed.presentation.syncweight

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.StatusInfo
import paufregi.connectfeed.presentation.ui.components.StatusInfoType
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun SyncWeightScreen(
    @PreviewParameter(SyncWeightStatePreview::class) state: SyncWeightState,
    onComplete: () -> Unit = {},
) {
    SimpleScaffold {
        when (state.process) {
            is ProcessState.Processing -> Loading(it)
            is ProcessState.Success -> StatusInfo(
                type = StatusInfoType.Success,
                text = state.process.message ?: "Sync succeeded",
                actionButton = { Button(text = "Ok", onClick = onComplete) },
                paddingValues = it
            )

            is ProcessState.Failure -> StatusInfo(
                type = StatusInfoType.Failure,
                text = state.process.reason,
                actionButton = { Button(text = "Ok", onClick = onComplete) },
                paddingValues = it
            )

            is ProcessState.Idle -> StatusInfo(
                type = StatusInfoType.Unknown,
                text = "Don't know what to do",
                actionButton = { Button(text = "Ok", onClick = onComplete) },
                paddingValues = it
            )
        }
    }
}
