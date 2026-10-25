package paufregi.connectfeed.presentation.syncweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.presentation.ui.models.Status
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SyncWeightViewModel @Inject constructor(
    getUser: GetUser,
    private val syncWeight: SyncWeight,
) : ViewModel() {
    private val _state = MutableStateFlow<Status>(Status.Loading)

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)
    val user = getUser().stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), null)

    // The Activity re-delivers the shared file on every recreation; only sync it once.
    private var started = false

    internal fun injectState(state: Status) {
        _state.value = state
    }

    fun updateWeight(inputStream: InputStream?) {
        if (started) {
            inputStream?.close()
            return
        }
        started = true

        viewModelScope.launch {
            _state.update { Status.Loading }
            if (inputStream == null) {
                _state.update { Status.Failure("nothing to sync") }
                return@launch
            }

            val weights = inputStream.use { stream ->
                RenphoReader.read(stream)
            }.onFailure {
                _state.update { Status.Failure("failed to read file") }
                return@launch
            }.getOrElse { emptyList() }

            if (weights.isEmpty()) {
                _state.update { Status.Failure("nothing to sync") }
                return@launch
            }

            syncWeight(weights)
                .onSuccess { _state.update { Status.Success("Sync succeeded") }}
                .onFailure { e -> _state.update { Status.Failure(e.message ?: "Unknown error") } }
        }
    }
}
