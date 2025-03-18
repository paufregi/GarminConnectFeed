package paufregi.connectfeed.presentation.syncweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.SyncWeight
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SyncWeightViewModel @Inject constructor(
    val syncWeightUseCase: SyncWeight
) : ViewModel() {

    private val _state = MutableStateFlow<SyncWeightState>(SyncWeightState.Idle)
    val state = _state.asStateFlow()

    fun syncWeight(inputStream: InputStream?) = viewModelScope.launch {
        _state.update { SyncWeightState.Uploading }
        if (inputStream == null) {
            _state.update { SyncWeightState.Failure }
            return@launch
        }
        syncWeightUseCase(inputStream)
            .onSuccess { _state.update { SyncWeightState.Success } }
            .onFailure { _state.update { SyncWeightState.Failure } }
    }
}
