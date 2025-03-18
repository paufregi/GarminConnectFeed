package paufregi.connectfeed.presentation.account

sealed interface AccountAction {
    data object RefreshUser : AccountAction
    data object SignOut : AccountAction
    data object StravaDisconnect : AccountAction
    data object Reset : AccountAction
}
