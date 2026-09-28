package paufregi.connectfeed.presentation.ui.models

sealed class AuthState {
    object Authenticated: AuthState()
    object NotAuthenticated: AuthState()
    object Loading: AuthState()
}
