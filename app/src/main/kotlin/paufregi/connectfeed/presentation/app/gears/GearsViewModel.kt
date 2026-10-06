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
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class GearsViewModel @Inject constructor(
    getGears: GetGears,
    val syncGear: SyncGear,
    val notification: NotificationManager,
) : ViewModel() {
    private val _state = MutableStateFlow(GearsState())

    val state = combine(_state, getGears()) { state, gears -> state.copy(gears = gears)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    fun sync() = viewModelScope.launch {
        _state.update { it.copy(loading = true) }
        syncGear()
            .onSuccess { notification.show("Gears synced") }
            .onFailure { err -> notification.show(err.message ?: "Sync failed") }
            .finally { _state.update { it.copy(loading = false) } }
    }
}

