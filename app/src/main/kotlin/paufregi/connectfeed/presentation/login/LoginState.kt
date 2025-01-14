package paufregi.connectfeed.presentation.login

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcessState

data class LoginState(
    val process: ProcessState = ProcessState.Idle,
    val username: String = "",
    val password: String = "",
    val user: User? = null,
    val showPassword: Boolean = false,
)
