package paufregi.connectfeed.presentation.app.login

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcState

data class LoginState(
    val process: ProcState? = null,
    val username: String = "paulfrancis.ellis@gmail.com",
    val password: String = "QEfNgWo9mHToGuCJapQs",
    val user: User? = null,
    val showPassword: Boolean = false,
)
