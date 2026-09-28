package paufregi.connectfeed.presentation.app.gears

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.SyncGear
import paufregi.connectfeed.presentation.ui.models.ProcState
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class GearsViewModel @Inject constructor(
    getGears: GetGears,
    val syncGear: SyncGear,
) : ViewModel() {
    private val _state = MutableStateFlow(GearsState())

    val state = combine(_state, getGears()) { state, gears -> state.copy(gears = gears)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), GearsState())

    fun onAction(action: GearsAction) {
        when (action) {
            GearsAction.Reset -> _state.update { it.copy(process = null) }
            GearsAction.Sync -> sync()
        }
    }

    private fun sync() = viewModelScope.launch {
        _state.update { it.copy(process = ProcState.Running) }
        syncGear()
            .onSuccess { _state.update { it.copy(process = ProcState.Success("Gears synced")) } }
            .onFailure { err -> _state.update { it.copy(process = ProcState.Failure(err.message ?: "Error")) } }
    }
}

