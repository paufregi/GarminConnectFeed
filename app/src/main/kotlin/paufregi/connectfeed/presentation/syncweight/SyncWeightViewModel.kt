package paufregi.connectfeed.presentation.syncweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.SyncStravaWeight
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.presentation.ui.models.ProcessState
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SyncWeightViewModel @Inject constructor(
    val syncWeight: SyncWeight,
    val syncStravaWeight: SyncStravaWeight
) : ViewModel() {

    private val _state = MutableStateFlow<SyncWeightState>(SyncWeightState())

    val state = _state.asStateFlow()

    fun updateWeight(inputStream: InputStream?) = viewModelScope.launch {
        _state.update { SyncWeightState(ProcessState.Processing) }
        if (inputStream == null) {
            _state.update { SyncWeightState(ProcessState.Failure("Nothing to sync")) }
            return@launch
        }

        val weightsRes = RenphoReader.read(inputStream)
        if (weightsRes.isFailure) {
            _state.update { SyncWeightState(ProcessState.Failure("Failed to read file")) }
            return@launch
        }
        val weights = weightsRes.getOrElse { emptyList() }

        if (weights.isEmpty()) {
            _state.update { SyncWeightState(ProcessState.Failure("Nothing to sync")) }
            return@launch
        }

        val errors = mutableListOf<String>()

        coroutineScope {
            val asyncSyncWeight = async { syncWeight(weights) }

            val asyncSyncStravaWeight = async { syncStravaWeight(weights) }

            runCatchingResult { asyncSyncWeight.await() }
                .onFailure { errors.add("Garmin") }

            runCatchingResult { asyncSyncStravaWeight.await() }
                .onFailure { errors.add("Strava") }
        }

        when (errors.isEmpty()) {
            true -> _state.update { SyncWeightState(ProcessState.Success("Sync succeeded")) }
            false -> _state.update { SyncWeightState(ProcessState.Failure("${errors.joinToString(" & ")} sync failed")) }
        }
    }
}
