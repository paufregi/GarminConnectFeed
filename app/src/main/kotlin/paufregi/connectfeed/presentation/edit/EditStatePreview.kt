package paufregi.connectfeed.presentation.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.presentation.ui.models.ProcessState

class EditStatePreview : PreviewParameterProvider<EditState> {
    override val values = sequenceOf(
        EditState(ProcessState.Processing),
        EditState(ProcessState.Success("Activity updates")),
        EditState(ProcessState.Failure("Failed to update activity")),
    )
}
