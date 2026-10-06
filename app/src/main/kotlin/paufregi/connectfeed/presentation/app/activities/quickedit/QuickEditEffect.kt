package paufregi.connectfeed.presentation.app.activities.quickedit

sealed interface QuickEditEffect {
    data object NavigateBack : QuickEditEffect
}