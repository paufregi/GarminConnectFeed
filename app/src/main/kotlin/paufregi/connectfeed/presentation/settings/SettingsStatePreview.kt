package paufregi.connectfeed.presentation.settings

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.presentation.ui.models.ProcessState

class SettingsStatePreview : PreviewParameterProvider<SettingsState> {
    override val values = sequenceOf(
        SettingsState(process = ProcessState.Processing),
        SettingsState(process = ProcessState.Success("Profile saved")),
        SettingsState(process = ProcessState.Failure("Error saving profile")),
        SettingsState(process = ProcessState.Idle, hasStrava = false),
        SettingsState(process = ProcessState.Idle, hasStrava = true),
    )
}