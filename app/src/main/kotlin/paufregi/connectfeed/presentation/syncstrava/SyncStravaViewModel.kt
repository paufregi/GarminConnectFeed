package paufregi.connectfeed.presentation.syncstrava

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.SyncStravaActivity
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class SyncStravaViewModel @Inject constructor(
    val getLatestActivities: GetLatestActivities,
    val getLatestStravaActivities: GetLatestStravaActivities,
    val syncActivity: SyncStravaActivity,
) : ViewModel() {

    private val _state = MutableStateFlow(SyncStravaState())

    val state = _state
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SyncStravaState())

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
            true -> _state.update {
                it.copy(process = ProcessState.Failure("Couldn't load ${errors.joinToString(" & ")}"))
            }

            false -> _state.update { it.copy(process = ProcessState.Idle) }
        }
    }

    fun onAction(event: SyncStravaAction) = when (event) {
        is SyncStravaAction.SetActivity -> _state.update {
            it.copy(
                activity = event.activity,
                stravaActivity = if ((it.stravaActivity == null) || (it.stravaActivity.type != event.activity.type))
                    it.stravaActivities.find { it.match(event.activity) }
                else
                    it.stravaActivity,
            )
        }

        is SyncStravaAction.SetStravaActivity -> _state.update {
            it.copy(
                stravaActivity = event.activity,
                activity = if ((it.activity == null) || (it.activity.type != event.activity.type))
                    it.activities.find { it.match(event.activity) }
                else
                    it.activity,
            )
        }

        is SyncStravaAction.SetDescription -> _state.update { it.copy(description = event.description) }
        is SyncStravaAction.SetTrainingEffect -> _state.update { it.copy(trainingEffect = event.trainingEffect) }
        is SyncStravaAction.Save -> saveActivity()
        is SyncStravaAction.Restart -> {
            _state.update { SyncStravaState() }
            load()
        }
    }

    private fun saveActivity() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        when (syncActivity(
            activity = state.value.activity,
            stravaActivity = state.value.stravaActivity,
            description = state.value.description,
            trainingEffect = state.value.trainingEffect
        )) {
            is Result.Success -> _state.update { it.copy(process = ProcessState.Success("Activity updated")) }
            is Result.Failure -> _state.update { it.copy(process = ProcessState.Failure("Couldn't update activity")) }
        }
    }
}