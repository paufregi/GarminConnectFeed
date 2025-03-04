package paufregi.connectfeed.presentation.quickedit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.IsStravaLoggedIn
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class QuickEditViewModel @Inject constructor(
    val getLatestActivities: GetLatestActivities,
    val getLatestStravaActivities: GetLatestStravaActivities,
    getProfiles: GetProfiles,
    isStravaLoggedIn: IsStravaLoggedIn,
    val updateActivity: UpdateActivity
) : ViewModel() {
    private val _state = MutableStateFlow(QuickEditState())
    val state = combine(_state, getProfiles(), isStravaLoggedIn()) { state, profiles, strava -> state.copy(profiles = profiles, hasStrava = strava) }
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), QuickEditState())

    fun onEvent(event: QuickEditEvent) = when (event) {
        is QuickEditEvent.SetActivity -> _state.update {
            it.copy(
                activity = event.activity,
                stravaActivity = if (it.stravaActivity?.type != event.activity.type) null else it.stravaActivity,
                profile = if (it.profile?.activityType != event.activity.type) null else it.profile,
            )
        }
        is QuickEditEvent.SetStravaActivity -> _state.update { it.copy(stravaActivity = event.activity) }
        is QuickEditEvent.SetProfile -> _state.update { it.copy( profile = event.profile ) }
        is QuickEditEvent.SetWater -> _state.update { it.copy( profile = it.profile?.copy(water = event.water) ) }
        is QuickEditEvent.SetEffort -> _state.update { it.copy( effort = if (event.effort == 0f) null else event.effort ) }
        is QuickEditEvent.SetFeel -> _state.update { it.copy( feel = event.feel ) }
        is QuickEditEvent.Save -> saveActivity()
        is QuickEditEvent.Restart -> {
            _state.update { QuickEditState() }
            load()
        }
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        val errors = mutableListOf<String>()

        getLatestActivities()
            .onSuccess { data -> _state.update { it.copy(activities = data) } }
            .onFailure { errors.add("activities") }

        getLatestStravaActivities()
            .onSuccess { data ->
                Log.i("QuickEditViewModel", "Strava activities: $data")
                _state.update { it.copy(stravaActivities = data) }
            }
            .onFailure { errors.add("Strava activities") }

        when (errors.isNotEmpty()) {
            true -> _state.update { it.copy(process = ProcessState.Failure("Couldn't load ${errors.joinToString(" & ")}")) }
            false -> _state.update { it.copy(process = ProcessState.Idle) }
        }
    }

    private fun saveActivity() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        updateActivity(
            activity = state.value.activity,
            profile = state.value.profile,
            feel = state.value.feel,
            effort = state.value.effort
        )
            .onSuccess { _state.update { it.copy(process = ProcessState.Success("Activity updated")) } }
            .onFailure { err -> _state.update { it.copy(process = ProcessState.Failure(err)) } }
    }
}