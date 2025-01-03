package paufregi.connectfeed.presentation.password

import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcessState

data class PasswordState(
    val process: ProcessState = ProcessState.Idle,
    val credential: Credential = Credential(), //Credential("paulfrancis.ellis@gmail.com", "FqkB${'$'}Te3F!#q"),
    val showPassword: Boolean = false,
)