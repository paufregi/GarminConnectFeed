package paufregi.connectfeed.presentation.strava

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.presentation.syncweight.SyncWeightState

class StravaStatePreview : PreviewParameterProvider<StravaState> {
    override val values = sequenceOf(
        StravaState.Processing,
        StravaState.Success,
        StravaState.Failure,
    )
}