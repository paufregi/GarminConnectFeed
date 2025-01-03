package paufregi.connectfeed.presentation.account


sealed interface AccountEvent {
    data object RefreshTokens: AccountEvent
    data object SignOut: AccountEvent
    data object Reset: AccountEvent
}
