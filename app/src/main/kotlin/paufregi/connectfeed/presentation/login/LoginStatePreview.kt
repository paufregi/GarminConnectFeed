package paufregi.connectfeed.presentation.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcessState

class LoginStatePreview : PreviewParameterProvider<LoginState> {
    override val values = sequenceOf(
        LoginState(process = ProcessState.Processing),
        LoginState(process = ProcessState.Success("Paul"), user = User(1, "Paul", "")),
        LoginState(process = ProcessState.Failure("Error")),
        LoginState(process = ProcessState.Idle, username = "user", password = "pass"),
        LoginState(process = ProcessState.Idle,username = "user", password = "pass", showPassword = true),
        LoginState(process = ProcessState.Idle,username = "", password = ""),
    )
}
