package paufregi.connectfeed.presentation.app.activities.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity

class ActivityPreview : PreviewParameterProvider<ActivityState> {
    override val values = sequenceOf(
        ActivityState(
            activity = Activity.EMPTY,
        ),
    )
}

