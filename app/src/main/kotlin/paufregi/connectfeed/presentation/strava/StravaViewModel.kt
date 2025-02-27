package paufregi.connectfeed.presentation.strava

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.SaveStravaCode
import paufregi.connectfeed.presentation.syncweight.SyncWeightState
import javax.inject.Inject

@HiltViewModel
class StravaViewModel @Inject constructor(
    val saveStravaCode: SaveStravaCode
) : ViewModel() {

    private val _state = MutableStateFlow<StravaState>(StravaState.Processing)
    val state = _state.asStateFlow()

    fun saveCode(code: String) = viewModelScope.launch {
        saveStravaCode(code)
        _state.update { StravaState.Success }
    }
}
