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
import paufregi.connectfeed.core.utils.finally
import paufregi.connectfeed.presentation.ui.utils.SnackbarManager
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class GearsViewModel @Inject constructor(
    getGears: GetGears,
    val syncGear: SyncGear,
    val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val _state = MutableStateFlow(GearsState())

    val state = combine(_state, getGears()) { state, gears -> state.copy(gears = gears)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), GearsState())

    fun onAction(action: GearsAction) {
        when (action) {
            GearsAction.Reset -> _state.update { it.copy(loading = false) }
            GearsAction.Sync -> sync()
        }
    }

    private fun sync() = viewModelScope.launch {
        _state.update { it.copy(loading = true) }
        syncGear()
            .onSuccess { snackbarManager.showMessage("Gears synced") }
            .onFailure { err -> snackbarManager.showMessage(err.message ?: "Sync failed") }
            .finally { _state.update { it.copy(loading = false) } }
    }
}

