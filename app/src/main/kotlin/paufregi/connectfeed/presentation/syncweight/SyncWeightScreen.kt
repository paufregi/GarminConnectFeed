package paufregi.connectfeed.presentation.syncweight

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.components.unknownInfo

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun SyncWeightScreen(
    @PreviewParameter(SyncWeightStatePreview::class) state: SyncWeightState,
    onComplete: () -> Unit = {},
) {
    Screen(
        state = state.process,
        success = successInfo(onComplete),
        failure = failureInfo(onComplete),
        content = unknownInfo(onComplete)
    )
}
