package paufregi.connectfeed.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.core.utils.getOrMatch
import paufregi.connectfeed.core.utils.getOrNull
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    val getActivities: GetActivities,
    val getStravaActivities: GetStravaActivities,
    val getEventTypes: GetEventTypes,
    val getCourses: GetCourses,
    val updateActivity: UpdateActivity,
    val updateStravaActivity: UpdateStravaActivity,
) : ViewModel() {

    private val _state = MutableStateFlow(EditState())

    val state = _state
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), EditState())

    private fun load(force: Boolean = false) = viewModelScope.launch {
        _state.update { it.copy(
            process = ProcessState.Processing,
            eventTypes = getEventTypes()
        ) }

        val errors = mutableListOf<String>()
        val activitiesErrors = mutableListOf<String>()
        val courseError = mutableListOf<String>()

        coroutineScope {
            val asyncGetActivities = async { getActivities(force) }
            val asyncGetStravaActivities = async { getStravaActivities(force) }
            val asyncGetCourses = async { getCourses(force) }

            runCatchingResult { asyncGetActivities.await() }
                .onSuccess { data -> _state.update { it.copy(activities = data) } }
                .onFailure { activitiesErrors.add("Garmin") }

            runCatchingResult { asyncGetStravaActivities.await() }
                .onSuccess { data -> _state.update { it.copy(stravaActivities = data) } }
                .onFailure { activitiesErrors.add("Strava") }

            runCatchingResult { asyncGetCourses.await() }
                .onSuccess { data -> _state.update { it.copy(courses = data) } }
                .onFailure { courseError.add("courses") }
        }

        if (activitiesErrors.isNotEmpty()) {
            errors.add("${activitiesErrors.joinToString(" & ")} activities")
        }
        errors.addAll(courseError)

        when (errors.isEmpty()) {
            true -> _state.update { it.copy(process = ProcessState.Idle) }
            false -> _state.update { it.copy(process = ProcessState.Failure("Couldn't load ${errors.joinToString(" & ")}")) }
        }
    }

    fun onAction(action: EditAction) = when (action) {
        is EditAction.SetActivity -> _state.update { it.copy(
            activity = action.activity,
            stravaActivity = it.stravaActivity.getOrMatch(action.activity, it.stravaActivities),
            course = it.course.getOrNull(action.activity)
        ) }
        is EditAction.SetStravaActivity -> _state.update { it.copy(
            stravaActivity = action.activity,
            activity = it.activity.getOrMatch(action.activity, it.activities),
            course = it.course.getOrNull(action.activity)
        ) }
        is EditAction.SetDescription -> _state.update { it.copy(description = action.description) }
        is EditAction.SetName -> _state.update { it.copy(name = action.name) }
        is EditAction.SetEventType -> _state.update { it.copy(eventType = action.eventType) }
        is EditAction.SetCourse -> _state.update { it.copy(
            course = action.course,
            activity = it.activity.getOrNull(action.course),
            stravaActivity = it.stravaActivity.getOrNull(action.course)
        ) }
        is EditAction.SetWater -> _state.update { it.copy(water = action.water) }
        is EditAction.SetEffort -> _state.update { it.copy(effort = action.effort.getOrNull()) }
        is EditAction.SetFeel -> _state.update { it.copy(feel = action.feel) }
        is EditAction.SetTrainingEffect -> _state.update { it.copy(trainingEffect = action.trainingEffect) }
        is EditAction.Save -> saveAction()
        is EditAction.Restart -> restartAction()
    }

    private fun saveAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        val errors = mutableListOf<String>()

        coroutineScope {
            val asyncUpdateActivity = async {
                updateActivity(
                    activity = state.value.activity,
                    name = state.value.name,
                    eventType = state.value.eventType,
                    course = state.value.course,
                    water = state.value.water,
                    feel = state.value.feel,
                    effort = state.value.effort
                )
            }

            val asyncUpdateStravaActivity = async {
                if (state.value.hasStrava && state.value.stravaActivity != null) {
                    updateStravaActivity(
                        stravaActivity = state.value.stravaActivity,
                        name = state.value.name,
                        description = state.value.description,
                        eventType = state.value.eventType,
                        trainingEffect = state.value.activity?.trainingEffect,
                        trainingEffectFlag = state.value.trainingEffect
                    )
                } else {
                    Result.success(Unit)
                }
            }

            runCatchingResult { asyncUpdateActivity.await() }
                .onFailure { errors.add("Garmin") }

            runCatchingResult { asyncUpdateStravaActivity.await() }
                .onFailure { errors.add("Strava") }
        }

        when (errors.isEmpty()) {
            true -> _state.update { it.copy(process = ProcessState.Success("Activity updated")) }
            false -> _state.update { it.copy(process = ProcessState.Failure("Couldn't update ${errors.joinToString(" & ")} activity")) }
        }
    }

    private fun restartAction() {
        _state.update { EditState() }
        load(true)
    }
}