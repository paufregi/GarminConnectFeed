package paufregi.connectfeed.presentation.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcessState

class LoginStatePreview : PreviewParameterProvider<LoginState> {
    override val values = sequenceOf(
        LoginState(username = "user", password = "pass"),
        LoginState(username = "user", password = "pass", showPassword = true),
        LoginState(username = "", password = ""),
        LoginState(user = User(1, "Paul", "https://profile.image.com/large.jpg"), process = ProcessState.Success() ),
    )
}
