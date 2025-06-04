package paufregi.connectfeed.presentation.strava

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.StravaCodeExchange
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class StravaViewModel @Inject constructor(
    val stravaCodeExchange: StravaCodeExchange
) : ViewModel() {

    private val _state = MutableStateFlow<StravaState>(StravaState())

    val state = _state.asStateFlow()

    fun exchangeToken(code: String) = viewModelScope.launch {
        _state.update { StravaState(ProcessState.Processing) }
        stravaCodeExchange(code)
            .onSuccess { _state.update { StravaState(ProcessState.Success("Strava linked")) } }
            .onFailure { _state.update { StravaState(ProcessState.Failure("Link failed")) } }
    }
}
