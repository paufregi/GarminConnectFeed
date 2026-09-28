package paufregi.connectfeed.presentation.syncweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.presentation.ui.models.ProcState
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SyncWeightViewModel @Inject constructor(
    val syncWeight: SyncWeight,
) : ViewModel() {
    private val _state = MutableStateFlow<ProcState>(ProcState.Running)

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), ProcState.Running)

    fun updateWeight(inputStream: InputStream?) = viewModelScope.launch {
        _state.update { ProcState.Running }
        if (inputStream == null) {
            _state.update { ProcState.Failure("Nothing to sync") }
            return@launch
        }

        val weights = inputStream.use { stream ->
            RenphoReader.read(stream)
        }.onFailure {
            _state.update { ProcState.Failure("Failed to read file") }
            return@launch
        }.getOrElse { emptyList() }

        if (weights.isEmpty()) {
            _state.update { ProcState.Failure("Nothing to sync") }
            return@launch
        }

        syncWeight(weights)
            .onSuccess { _state.update { ProcState.Success("Sync succeeded") }}
            .onFailure { e -> _state.update { ProcState.Failure(e.message ?: "Sync failed") } }
    }
}
