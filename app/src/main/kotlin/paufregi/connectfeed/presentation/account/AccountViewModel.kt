package paufregi.connectfeed.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.DisconnectStrava
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.IsStravaLoggedIn
import paufregi.connectfeed.core.usecases.RefreshUser
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    getUser: GetUser,
    isStravaLoggedIn: IsStravaLoggedIn,
    val refreshUserUseCase: RefreshUser,
    val signOutUseCase: SignOut,
    val disconnectStrava: DisconnectStrava,
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())

    val state = combine(
        _state,
        getUser(),
        isStravaLoggedIn()
    ) { state, user, strava -> state.copy(user = user, hasStrava = strava) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), AccountState())

    fun onAction(action: AccountAction) = when (action) {
        is AccountAction.RefreshUser -> refreshUser()
        is AccountAction.SignOut -> signOut()
        is AccountAction.StravaDisconnect -> signOutStrava()
        is AccountAction.Reset -> _state.update { AccountState() }
    }

    private fun refreshUser() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        refreshUserUseCase()
            .onSuccess { _state.update { AccountState(ProcessState.Success("User data refreshed")) } }
            .onFailure { err -> _state.update { AccountState(ProcessState.Failure(err)) } }
    }

    private fun signOut() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        signOutUseCase()
    }

    private fun signOutStrava() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        disconnectStrava()
        _state.update { AccountState(ProcessState.Idle) }
    }
}