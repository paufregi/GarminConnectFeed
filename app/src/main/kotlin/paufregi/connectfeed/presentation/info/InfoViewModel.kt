package paufregi.connectfeed.presentation.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.core.usecases.GetLatestRelease
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class InfoViewModel @Inject constructor(
    @param:Named("currentVersion") val version: String,
    val getLatestRelease: GetLatestRelease,
) : ViewModel() {

    private val _state = MutableStateFlow(InfoState(currentVersion = Version.parse(version)))

    val state = _state.asStateFlow()

    fun onAction(action: InfoAction) {
        when(action) {
            InfoAction.CheckUpdate -> checkUpdatesAction()
            InfoAction.Update -> {}
        }
    }

    private fun checkUpdatesAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing)}

        getLatestRelease()
            .onSuccess { data -> _state.update { it.copy(latestRelease = data, process = ProcessState.Idle) } }
            .onFailure { _state.update { it.copy(latestRelease = null, process = ProcessState.Failure("Couldn't load latest release")) } }
    }

}