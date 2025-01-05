package paufregi.connectfeed.presentation.account


sealed interface AccountEvent {
    data object ClearTokens: AccountEvent
    data object SignOut: AccountEvent
    data object Reset: AccountEvent
}
