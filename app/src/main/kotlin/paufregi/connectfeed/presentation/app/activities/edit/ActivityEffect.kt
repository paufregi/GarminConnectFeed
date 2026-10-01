package paufregi.connectfeed.presentation.app.activities.edit

sealed interface ActivityEffect {
    data object NavigateBack : ActivityEffect
}