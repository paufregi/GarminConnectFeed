package paufregi.connectfeed.presentation.ui.models

sealed interface Status {
    data object Loading : Status
    data class Success(val message: String? = null) : Status
    data class Failure(val reason: String) : Status
}