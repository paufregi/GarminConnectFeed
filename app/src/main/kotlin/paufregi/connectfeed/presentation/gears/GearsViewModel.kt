package paufregi.connectfeed.presentation.gears

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.IsStravaLoggedIn
import javax.inject.Inject

@HiltViewModel
class GearsViewModel @Inject constructor(
    isStravaLoggedIn: IsStravaLoggedIn,
    val getGears: GetGears,
) : ViewModel() {

    private val _state = MutableStateFlow(GearsState())

    val state = combine(_state, isStravaLoggedIn(),
    ) { state, strava -> state.copy(hasStrava = strava) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), GearsState())
}