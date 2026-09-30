package paufregi.connectfeed.presentation.app.profiles.edit

sealed interface ProfileEffect {
    data object NavigateBack : ProfileEffect
}