package paufregi.connectfeed.presentation.strava

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.StravaCodeExchange
import javax.inject.Inject

@HiltViewModel
class StravaViewModel @Inject constructor(
    val stravaCodeExchange: StravaCodeExchange
) : ViewModel() {

    private val _state = MutableStateFlow<StravaState>(StravaState.Processing)
    val state = _state.asStateFlow()

    fun exchangeToken(code: String) = viewModelScope.launch {
        when (stravaCodeExchange(code)) {
            is Result.Success -> _state.update { StravaState.Success }
            is Result.Failure -> _state.update { StravaState.Failure }
        }
    }
}
