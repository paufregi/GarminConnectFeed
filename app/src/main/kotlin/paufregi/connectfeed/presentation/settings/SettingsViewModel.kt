package paufregi.connectfeed.presentation.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.core.usecases.DisconnectStrava
import paufregi.connectfeed.core.usecases.GetLatestRelease
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.IsStravaLoggedIn
import paufregi.connectfeed.core.usecases.RefreshUser
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.system.Downloader
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getUser: GetUser,
    isStravaLoggedIn: IsStravaLoggedIn,
    val getLatestRelease: GetLatestRelease,
    val refreshUser: RefreshUser,
    val signOut: SignOut,
    val disconnectStrava: DisconnectStrava,
    @param:Named("currentVersion") val version: String,
    @param:Named("downloader") val downloader: Downloader,
    @param:Named("StravaAuthUri") val stravaAuthUri: Uri
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState(currentVersion = Version.parse(version)))

    val state = combine(
        _state,
        getUser(),
        isStravaLoggedIn(),
    ) { state, user, strava -> state.copy(user = user, hasStrava = strava) }
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), SettingsState())

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(updating = true) }

        getLatestRelease()
            .onSuccess { rel -> _state.update { it.copy(latestRelease = rel, updating = false) } }
            .onFailure { _state.update { it.copy(updating = false)  } }
    }

    fun onAction(action: SettingsAction) = when (action) {
        is SettingsAction.RefreshUser -> refreshUserAction()
        is SettingsAction.SignOut -> signOutAction()
        is SettingsAction.StravaDisconnect -> signOutStravaAction()
        is SettingsAction.Update -> updateAction()
        is SettingsAction.Reset -> _state.update { it.copy(process = ProcessState.Idle) }
    }

    private fun refreshUserAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        refreshUser()
            .onSuccess { _state.update { it.copy(process = ProcessState.Success("User data refreshed")) } }
            .onFailure { err -> _state.update { it.copy(process = ProcessState.Failure(err.message ?: "Error")) } }
    }

    private fun signOutAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        disconnectStrava()
        signOut()
        _state.update { it.copy(process = ProcessState.Idle) }
    }

    private fun signOutStravaAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        disconnectStrava()
        _state.update { it.copy(process = ProcessState.Idle) }
    }

    private fun updateAction() = viewModelScope.launch {
        state.value.latestRelease?.let {
            _state.update { it.copy(updating = true) }
            downloader.downloadApk(it)
        }
    }
}