package paufregi.connectfeed.presentation.app.activities.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity

class EditPreview : PreviewParameterProvider<EditState> {
    override val values = sequenceOf(
        EditState(
            activity = Activity.EMPTY,
        ),
    )
}

