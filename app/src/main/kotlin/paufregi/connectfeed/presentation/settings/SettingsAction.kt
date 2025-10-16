package paufregi.connectfeed.presentation.settings

sealed interface SettingsAction {
    data object RefreshUser : SettingsAction
    data object SignOut : SettingsAction
    data object CheckUpdate : SettingsAction
    data object Update : SettingsAction
    data object StravaDisconnect : SettingsAction
    data object Reset : SettingsAction
}
