package paufregi.connectfeed.presentation.strava

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.ConnectStrava
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.presentation.ui.models.ProcState
import javax.inject.Inject

@HiltViewModel
class StravaViewModel @Inject constructor(
    val getUser: GetUser,
    val enableStrava: ConnectStrava
) : ViewModel() {

    private val _state = MutableStateFlow<ProcState>(ProcState.Running)

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    fun exchangeToken(code: String) = viewModelScope.launch {
        _state.update { ProcState.Running }

        enableStrava(code)
            .onSuccess { _state.update { ProcState.Success("Strava linked") } }
            .onFailure { _state.update { ProcState.Failure("Link failed") } }
    }
}
