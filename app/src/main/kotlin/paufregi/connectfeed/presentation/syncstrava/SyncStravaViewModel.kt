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
import paufregi.connectfeed.core.utils.getOrMatch
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

        when (errors.isEmpty()) {
            true -> _state.update { it.copy(process = ProcessState.Idle) }
            false -> _state.update { it.copy(process = ProcessState.Failure("Couldn't load ${errors.joinToString(" & ")}")) }
        }
    }

    fun onAction(action: SyncStravaAction) = when (action) {
        is SyncStravaAction.SetActivity -> _state.update {
            it.copy(
                activity = action.activity,
                stravaActivity = it.stravaActivity.getOrMatch(action.activity, it.stravaActivities),
            )
        }
        is SyncStravaAction.SetStravaActivity -> _state.update {
            it.copy(
                stravaActivity = action.activity,
                activity = it.activity.getOrMatch(action.activity, it.activities),
            )
        }
        is SyncStravaAction.SetDescription -> _state.update { it.copy(description = action.description) }
        is SyncStravaAction.SetTrainingEffect -> _state.update { it.copy(trainingEffect = action.trainingEffect) }
        is SyncStravaAction.Save -> saveAction()
        is SyncStravaAction.Restart -> restartAction()
    }

    private fun saveAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        when (syncActivity(
            activity = state.value.activity,
            stravaActivity = state.value.stravaActivity,
            description = state.value.description,
            trainingEffect = state.value.trainingEffect
        )) {
            is Result.Success -> { _state.update { it.copy(process = ProcessState.Success("Activity updated")) } }
            is Result.Failure -> { _state.update { it.copy(process = ProcessState.Failure("Couldn't update activity")) } }
        }
    }

    private fun restartAction() {
        _state.update { SyncStravaState() }
        load()
    }
}