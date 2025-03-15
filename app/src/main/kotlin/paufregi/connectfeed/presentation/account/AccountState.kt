package paufregi.connectfeed.presentation.account

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.models.ProcessState

data class AccountState(
    val process: ProcessState = ProcessState.Idle,
    val user: User? = null,
    val hasStrava: Boolean? = null,
)
