package paufregi.connectfeed.presentation.app.login

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcState

data class LoginState(
    val process: ProcState? = null,
    val username: String = "",
    val password: String = "",
    val user: User? = null,
    val showPassword: Boolean = false,
)
