package paufregi.connectfeed.presentation.ui.models

sealed interface ProcState {
    data object Running : ProcState
    data class Success(val message: String? = null) : ProcState
    data class Failure(val reason: String) : ProcState
}