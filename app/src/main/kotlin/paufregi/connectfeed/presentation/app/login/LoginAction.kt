package paufregi.connectfeed.presentation.app.login


sealed interface LoginAction {
    data class SetUsername(val username: String) : LoginAction
    data class SetPassword(val password: String) : LoginAction
    data class ShowPassword(val showPassword: Boolean) : LoginAction
    data object SignIn : LoginAction
    data object Reset : LoginAction
}
