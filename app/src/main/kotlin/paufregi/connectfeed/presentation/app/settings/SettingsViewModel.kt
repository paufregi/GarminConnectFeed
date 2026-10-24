package paufregi.connectfeed.presentation.app.settings

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
import paufregi.connectfeed.core.usecases.IsStravaConnected
import paufregi.connectfeed.core.usecases.RefreshUser
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.core.utils.finally
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.system.Downloader
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUser: GetUser,
    private val isStravaConnected: IsStravaConnected,
    private val getLatestRelease: GetLatestRelease,
    private val refreshUser: RefreshUser,
    private val signOut: SignOut,
    private val disconnectStrava: DisconnectStrava,
    private val notification: NotificationManager,
    @param:Named("currentVersion") val version: String,
    @param:Named("downloader") val downloader: Downloader,
    @param:Named("StravaAuthUri") val stravaAuthUri: Uri,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState(currentVersion = Version.parse(version)))

    val state = combine(
        _state,
        getUser(),
        isStravaConnected(),
    ) { state, user, strava -> state.copy(user = user, hasStrava = strava) }
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    internal fun injectState(state: SettingsState) {
        _state.value = state
    }

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
        is SettingsAction.Reset -> _state.update { it.copy(loading = false) }
    }

    private fun refreshUserAction() = viewModelScope.launch {
        _state.update { it.copy(loading = true) }
        refreshUser()
            .onSuccess { notification.show("User data refreshed") }
            .onFailure { notification.show("Failed to refresh user data: ${it.message}") }
            .finally { _state.update { it.copy(loading = false) } }
    }

    private fun signOutAction() = viewModelScope.launch {
        _state.update { it.copy(loading = true) }
        disconnectStrava()
        signOut()
        _state.update { it.copy(loading = false) }
    }

    private fun signOutStravaAction() = viewModelScope.launch {
        _state.update { it.copy(loading = true) }
        disconnectStrava()
        _state.update { it.copy(loading = false) }
    }

    private fun updateAction() = viewModelScope.launch {
        state.value.latestRelease?.let { release ->
            _state.update { it.copy(updating = true) }
            downloader.downloadApk(release)
        }
    }
}