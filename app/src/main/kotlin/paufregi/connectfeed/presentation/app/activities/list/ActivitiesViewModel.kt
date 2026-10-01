package paufregi.connectfeed.presentation.app.activities.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.core.utils.finally
import paufregi.connectfeed.presentation.ui.utils.SnackbarManager
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ActivitiesViewModel @Inject constructor(
    val getActivities: GetActivities,
    val notificationManager: SnackbarManager,
) : ViewModel() {
    private val _state = MutableStateFlow(ActivitiesState())

    val state = _state
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), ActivitiesState())

    private fun load() = viewModelScope.launch{
        _state.update { it.copy(loading = true) }

        getActivities()
            .onSuccess { activities -> _state.update { it.copy(activities = activities) } }
            .onFailure { notificationManager.showMessage("Failed to load activities: ${it.message}") }
            .finally { _state.update { it.copy(loading = false) } }
    }

    fun onAction(action: ActivitiesAction) {
    }
}

