package paufregi.connectfeed.presentation.setup

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import paufregi.connectfeed.core.models.Credential

data class SetupState(
    val processState: ProcessState = ProcessState.Idle,
    val credential: Credential = Credential(),
    val showPassword: Boolean = false,
)

fun MutableStateFlow<SetupState>.change(
    processState: ProcessState? = null,
    username: String? = null,
    password: String? = null,
    credential: Credential? = null,
    showPassword: Boolean?  = null,
) = this.update { it.copy(
        processState = processState ?: it.processState,
        credential = credential ?: Credential(
            username ?: it.credential.username,
            password ?: it.credential.password),
        showPassword = showPassword ?: it.showPassword)
}


sealed interface ProcessState {
    data object Idle : ProcessState
    data object Processing : ProcessState
    data class Success(val name: String) : ProcessState
    data class Failure(val reason: String) : ProcessState
}