package paufregi.connectfeed.presentation.gears

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.SyncGear
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class GearsViewModel @Inject constructor(
    getGears: GetGears,
    private val syncGear: SyncGear,
) : ViewModel() {
    private val process = MutableStateFlow<ProcessState>(ProcessState.Idle)

    val state = combine(getGears(), process) { gears, process ->
        GearsState(gears = gears, process = process)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), GearsState())

    fun onAction(action: GearsAction) {
        when (action) {
            GearsAction.Reset -> process.update { ProcessState.Idle }
            GearsAction.Sync -> sync()
        }
    }

    private fun sync() = viewModelScope.launch {
        process.update { ProcessState.Processing }
        syncGear()
            .onSuccess { process.update { ProcessState.Success("Gears synced") } }
            .onFailure { err -> process.update { ProcessState.Failure(err.message ?: "Error") } }
    }
}

