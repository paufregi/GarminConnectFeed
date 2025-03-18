package paufregi.connectfeed.presentation.login


sealed interface LoginAction {
    data class SetUsername(val username: String) : LoginAction
    data class SetPassword(val password: String) : LoginAction
    data class ShowPassword(val showPassword: Boolean) : LoginAction
    data object Reset : LoginAction
    data object SignIn : LoginAction
}
