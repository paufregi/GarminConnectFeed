package paufregi.connectfeed.presentation.app.login

import paufregi.connectfeed.core.models.User

sealed interface Status {
    data object Loading : Status
    data object Success: Status
    data class Failure(val reason: String) : Status
}


data class LoginState(
    val status: Status? = null,
    val username: String = "paulfrancis.ellis@gmail.com",
    val password: String = "QEfNgWo9mHToGuCJapQs",
    val user: User? = null,
    val showPassword: Boolean = false,
)
