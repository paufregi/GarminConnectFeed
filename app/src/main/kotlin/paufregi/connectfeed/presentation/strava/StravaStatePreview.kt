package paufregi.connectfeed.presentation.strava

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.presentation.ui.models.ProcessState

class StravaStatePreview : PreviewParameterProvider<StravaState> {
    override val values = sequenceOf(
        StravaState(ProcessState.Processing),
        StravaState(ProcessState.Idle),
        StravaState(ProcessState.Success("Strava linked")),
        StravaState(ProcessState.Failure("Link failed")),
    )
}