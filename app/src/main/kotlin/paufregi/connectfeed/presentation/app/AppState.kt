package paufregi.connectfeed.presentation.app

import paufregi.connectfeed.core.models.User

sealed interface AppState {
    data class Authenticated(val user: User): AppState
    data object NotAuthenticated: AppState
    data object Loading: AppState
}