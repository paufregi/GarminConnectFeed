package paufregi.connectfeed.presentation.account


sealed interface AccountEvent {
    data object RefreshUser: AccountEvent
    data object SignOut: AccountEvent
    data object StravaDisconnect: AccountEvent
    data object Reset: AccountEvent
}
