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
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class QuickEditViewModel @Inject constructor(
    val getLatestActivities: GetLatestActivities,
    val getLatestStravaActivities: GetLatestStravaActivities,
    getProfiles: GetProfiles,
    val updateActivity: UpdateActivity,
    val updateStravaActivity: UpdateStravaActivity
) : ViewModel() {
    private val _state = MutableStateFlow(QuickEditState())
    val state = combine(_state, getProfiles()) { state, profiles -> state.copy(profiles = profiles) }
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
        is QuickEditEvent.SetStravaActivity -> _state.update {
            it.copy(
                stravaActivity = event.activity,
                activity = if (it.activity?.type != event.activity.type) null else it.activity,
                profile = if (it.profile?.activityType != event.activity.type) null else it.profile,
            )
        }
        is QuickEditEvent.SetProfile -> _state.update { it.copy( profile = event.profile ) }
        is QuickEditEvent.SetDescription -> _state.update { it.copy( description = event.description ) }
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
            .onSuccess { data -> _state.update { it.copy(stravaActivities = data) } }
            .onFailure { errors.add("Strava activities") }

        when (errors.isNotEmpty()) {
            true -> _state.update { it.copy(process = ProcessState.Failure("Couldn't load ${errors.joinToString(" & ")}")) }
            false -> _state.update { it.copy(process = ProcessState.Idle) }
        }
    }

    private fun saveActivity() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        val errors = mutableListOf<String>()
        updateActivity(
            activity = state.value.activity,
            profile = state.value.profile,
            feel = state.value.feel,
            effort = state.value.effort
        ).onFailure { errors.add("Garmin") }

        if (state.value.stravaActivities.isNotEmpty() &&  state.value.stravaActivity != null) {
            updateStravaActivity(
                activity = state.value.activity,
                stravaActivity = state.value.stravaActivity,
                profile = state.value.profile,
                description = state.value.description,
            ).onFailure { errors.add("Strava") }
        }

        when (errors.isNotEmpty()) {
            false -> _state.update { it.copy(process = ProcessState.Success("Activity updated")) }
            true -> _state.update { it.copy(process = ProcessState.Failure("Couldn't update ${errors.joinToString(" & ")} activity")) }
        }
    }
}