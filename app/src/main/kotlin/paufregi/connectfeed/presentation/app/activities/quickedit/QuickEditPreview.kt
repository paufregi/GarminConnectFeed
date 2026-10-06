package paufregi.connectfeed.presentation.app.activities.quickedit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity

class QuickEditPreview : PreviewParameterProvider<QuickEditState> {
    override val values = sequenceOf(
        QuickEditState(
            activity = Activity.EMPTY,
        ),
    )
}

