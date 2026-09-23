package paufregi.connectfeed.presentation.syncweight

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.presentation.ui.models.ProcessState
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SyncWeightViewModel @Inject constructor(
    val syncWeight: SyncWeight,
) : ViewModel() {

    private val _state = MutableStateFlow(SyncWeightState())

    val state = _state.asStateFlow()

    fun updateWeight(inputStream: InputStream?) = viewModelScope.launch {
        _state.update { SyncWeightState(ProcessState.Processing) }
        if (inputStream == null) {
            _state.update { SyncWeightState(ProcessState.Failure("Nothing to sync")) }
            return@launch
        }

        val weights = RenphoReader.read(inputStream).onFailure {
            _state.update { SyncWeightState(ProcessState.Failure("Failed to read file")) }
            return@launch
        }.getOrElse { emptyList() }

        if (weights.isEmpty()) {
            _state.update { SyncWeightState(ProcessState.Failure("Nothing to sync")) }
            return@launch
        }

        syncWeight(weights)
            .onSuccess { _state.update { SyncWeightState(ProcessState.Success("Sync succeeded")) }}
            .onFailure { e -> _state.update { SyncWeightState(ProcessState.Failure(e.message ?: "Sync failed")) } }
    }
}
