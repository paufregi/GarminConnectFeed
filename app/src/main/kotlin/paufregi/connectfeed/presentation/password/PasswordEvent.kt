package paufregi.connectfeed.presentation.password


sealed interface PasswordEvent {
    data class SetPassword(val password: String): PasswordEvent
    data class ShowPassword(val showPassword: Boolean): PasswordEvent
    data object SignIn: PasswordEvent
}
