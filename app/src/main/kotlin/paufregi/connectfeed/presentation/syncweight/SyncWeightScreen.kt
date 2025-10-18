package paufregi.connectfeed.presentation.syncweight

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.components.unknownInfo

@Composable
@ExperimentalMaterial3Api
internal fun SyncWeightScreen(
    state: SyncWeightState,
    onComplete: () -> Unit = {},
) {
    Screen(
        state = state.process,
        success = successInfo(onComplete),
        failure = failureInfo(onComplete),
        content = unknownInfo(onComplete)
    )
}
