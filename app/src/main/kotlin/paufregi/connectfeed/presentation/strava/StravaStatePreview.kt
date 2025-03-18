package paufregi.connectfeed.presentation.strava

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class StravaStatePreview : PreviewParameterProvider<StravaState> {
    override val values = sequenceOf(
        StravaState.Processing,
        StravaState.Success,
        StravaState.Failure,
    )
}