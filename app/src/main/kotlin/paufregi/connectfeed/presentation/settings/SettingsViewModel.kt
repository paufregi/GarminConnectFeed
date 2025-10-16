package paufregi.connectfeed.presentation.settings

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
class SettingsViewModel @Inject constructor(
    getUser: GetUser,
    isStravaLoggedIn: IsStravaLoggedIn,
    val refreshUser: RefreshUser,
    val signOut: SignOut,
    val disconnectStrava: DisconnectStrava,
    @param:Named("StravaAuthUri") val stravaAuthUri: Uri
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())

    val state = combine(
        _state,
        getUser(),
        isStravaLoggedIn()
    ) { state, user, strava -> state.copy(user = user, hasStrava = strava) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), SettingsState())

    fun onAction(action: SettingsAction) = when (action) {
        is SettingsAction.RefreshUser -> refreshUserAction()
        is SettingsAction.SignOut -> signOutAction()
        is SettingsAction.StravaDisconnect -> signOutStravaAction()
        is SettingsAction.Reset -> _state.update { SettingsState() }
        is SettingsAction.CheckUpdate -> {}
        is SettingsAction.Update -> {}
    }

    private fun refreshUserAction() = viewModelScope.launch {
        _state.update { SettingsState(ProcessState.Processing) }
        refreshUser()
            .onSuccess { _state.update { SettingsState(ProcessState.Success("User data refreshed")) } }
            .onFailure { err -> _state.update { SettingsState(ProcessState.Failure(err.message ?: "Error")) } }
    }

    private fun signOutAction() = viewModelScope.launch {
        _state.update { SettingsState(ProcessState.Processing) }
        signOut()
        _state.update { SettingsState(ProcessState.Idle) }
    }

    private fun signOutStravaAction() = viewModelScope.launch {
        _state.update { SettingsState(ProcessState.Processing) }
        disconnectStrava()
        _state.update { SettingsState(ProcessState.Idle) }
    }
}