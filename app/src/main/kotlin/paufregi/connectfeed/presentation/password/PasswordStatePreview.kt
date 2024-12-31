package paufregi.connectfeed.presentation.password

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.presentation.ui.models.ProcessState

class PasswordContentStatePreview : PreviewParameterProvider<PasswordState> {
    override val values = sequenceOf(
        PasswordState(process = ProcessState.Processing),
        PasswordState(process = ProcessState.Success("Paul")),
        PasswordState(process = ProcessState.Failure("Error")),
    )
}

class PasswordFormStatePreview : PreviewParameterProvider<PasswordState> {
    override val values = sequenceOf(
        PasswordState(credential = Credential("user", "pass")),
        PasswordState(credential = Credential("user", "pass"), showPassword = true),
        PasswordState(credential = Credential("user", "")),
    )
}