package paufregi.connectfeed.presentation.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcessState

class LoginContentStatePreview : PreviewParameterProvider<LoginState> {
    override val values = sequenceOf(
        LoginState(process = ProcessState.Processing),
        LoginState(process = ProcessState.Success("Paul"), user = User("Paul", "")),
        LoginState(process = ProcessState.Failure("Error")),
    )
}

class LoginFormStatePreview : PreviewParameterProvider<LoginState> {
    override val values = sequenceOf(
        LoginState(credential = Credential("user", "pass")),
        LoginState(credential = Credential("user", "")),
        LoginState(credential = Credential("", "pass")),
        LoginState(credential = Credential("user", "pass"), showPassword = true),
    )
}

