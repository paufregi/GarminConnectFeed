package paufregi.connectfeed.presentation.syncweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.SyncStravaWeight
import paufregi.connectfeed.core.usecases.SyncWeight
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
        val errors = mutableListOf<String>()

        if (inputStream == null) {
            _state.update { SyncWeightState(ProcessState.Failure("Nothing to sync")) }
            return@launch
        }

        syncWeight(inputStream)
            .onFailure { errors.add("Garmin") }

        syncStravaWeight(inputStream)
            .onFailure { errors.add("Strava") }

        when (errors.isEmpty()) {
            true -> _state.update { SyncWeightState(ProcessState.Success()) }
            false -> _state.update { SyncWeightState(ProcessState.Failure("${errors.joinToString(" & ")} sync failed")) }
        }
    }
}
