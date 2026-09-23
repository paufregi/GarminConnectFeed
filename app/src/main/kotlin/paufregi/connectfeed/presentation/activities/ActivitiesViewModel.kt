package paufregi.connectfeed.presentation.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ActivitiesViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow(ActivitiesState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), ActivitiesState())
}

