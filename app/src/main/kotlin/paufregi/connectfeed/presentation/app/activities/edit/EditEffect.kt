package paufregi.connectfeed.presentation.app.activities.edit

sealed interface EditEffect {
    data object NavigateBack : EditEffect
}