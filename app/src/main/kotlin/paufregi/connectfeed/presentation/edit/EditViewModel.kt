package paufregi.connectfeed.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.core.utils.getOrMatch
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    val getLatestActivities: GetLatestActivities,
    val getLatestStravaActivities: GetLatestStravaActivities,
    val getEventTypes: GetEventTypes,
    val getCourses: GetCourses,
    val updateActivity: UpdateActivity,
    val updateStravaActivity: UpdateStravaActivity,
) : ViewModel() {

    private val _state = MutableStateFlow(EditState())

    val state = _state
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), EditState())

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(
            process = ProcessState.Processing,
            eventTypes = getEventTypes()
        ) }

        val errors = mutableListOf<String>()

        getLatestActivities()
            .onSuccess { data -> _state.update { it.copy(activities = data) } }
            .onFailure { errors.add("activities") }

        getLatestStravaActivities()
            .onSuccess { data -> _state.update { it.copy(stravaActivities = data) } }
            .onFailure { errors.add("Strava activities") }

        getCourses()
            .onSuccess { data -> _state.update { it.copy(courses = data) } }
            .onFailure { errors.add("courses") }

        when (errors.isNotEmpty()) {
            true -> _state.update { it.copy(process = ProcessState.Failure("Couldn't load ${errors.joinToString(" & ")}")) }
            false -> _state.update { it.copy(process = ProcessState.Idle) }
        }
    }

    fun onAction(action: EditAction) = when (action) {
        is EditAction.SetActivity -> _state.update {
            it.copy(
                activity = action.activity,
                stravaActivity = it.stravaActivity.getOrMatch(action.activity, it.stravaActivities),
            )
        }
        is EditAction.SetStravaActivity -> _state.update {
            it.copy(
                stravaActivity = action.activity,
                activity = it.activity.getOrMatch(action.activity, it.activities),
            )
        }
        is EditAction.SetDescription -> _state.update { it.copy(description = action.description) }
        is EditAction.SetName -> _state.update { it.copy(name = action.name) }
        is EditAction.SetEventType -> _state.update { it.copy(eventType = action.eventType) }
        is EditAction.SetCourse -> _state.update { it.copy(course = action.course) }
        is EditAction.SetWater -> _state.update { it.copy(water = action.water) }
        is EditAction.SetEffort -> _state.update { it.copy(effort = if (action.effort == 0f) null else action.effort) }
        is EditAction.SetFeel -> _state.update { it.copy(feel = action.feel) }
        is EditAction.SetTrainingEffect -> _state.update { it.copy(trainingEffect = action.trainingEffect) }
        is EditAction.Save -> saveActivity()
        is EditAction.Restart -> {
            _state.update { EditState() }
            load()
        }
    }

    private fun saveActivity() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        val errors = mutableListOf<String>()

        when (errors.isNotEmpty()) {
            false -> _state.update { it.copy(process = ProcessState.Success("Activity updated")) }
            true -> _state.update {
                it.copy(process = ProcessState.Failure("Couldn't update ${errors.joinToString(" & ")}"))
            }
        }
    }
}