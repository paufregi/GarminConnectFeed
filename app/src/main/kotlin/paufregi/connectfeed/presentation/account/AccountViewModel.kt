package paufregi.connectfeed.presentation.account

import android.net.Uri
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
import javax.inject.Named

@HiltViewModel
class AccountViewModel @Inject constructor(
    getUser: GetUser,
    isStravaLoggedIn: IsStravaLoggedIn,
    val refreshUser: RefreshUser,
    val signOut: SignOut,
    val disconnectStrava: DisconnectStrava,
    @Named("StravaAuthUri")val stravaAuthUri: Uri
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())

    val state = combine(
        _state,
        getUser(),
        isStravaLoggedIn()
    ) { state, user, strava -> state.copy(user = user, hasStrava = strava) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), AccountState())

    fun onAction(action: AccountAction) = when (action) {
        is AccountAction.RefreshUser -> refreshUserAction()
        is AccountAction.SignOut -> signOutAction()
        is AccountAction.StravaDisconnect -> signOutStravaAction()
        is AccountAction.Reset -> _state.update { AccountState() }
    }

    private fun refreshUserAction() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        refreshUser()
            .onSuccess { _state.update { AccountState(ProcessState.Success("User data refreshed")) } }
            .onFailure { err -> _state.update { AccountState(ProcessState.Failure(err.message ?: "Error")) } }
    }

    private fun signOutAction() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        signOut()
        _state.update { AccountState(ProcessState.Idle) }
    }

    private fun signOutStravaAction() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        disconnectStrava()
        _state.update { AccountState(ProcessState.Idle) }
    }
}