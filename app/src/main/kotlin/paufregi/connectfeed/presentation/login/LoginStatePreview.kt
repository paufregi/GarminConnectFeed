package paufregi.connectfeed.presentation.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class LoginStatePreview : PreviewParameterProvider<LoginState> {
    override val values = sequenceOf(
        LoginState(username = "user", password = "pass"),
        LoginState(username = "user", password = "pass", showPassword = true),
        LoginState(username = "", password = ""),
    )
}
