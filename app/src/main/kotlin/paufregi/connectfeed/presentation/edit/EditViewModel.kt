package paufregi.connectfeed.presentation.edit

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.core.utils.updateIf
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class EditViewModel @Inject constructor(
    val getActivities: GetActivities,
    val getStravaActivities: GetStravaActivities,
    val getGears: GetGears,
    val getEventTypes: GetEventTypes,
    val getCourses: GetCourses,
    val getWorkout: GetWorkout,
    val updateActivity: UpdateActivity,
    val updateStravaActivity: UpdateStravaActivity,
) : ViewModel() {

    @VisibleForTesting
    internal fun seedStateForTest(state: EditState) {
        _state.value = state
        autoLoad = false
    }

    private var autoLoad = true

    private val _state = MutableStateFlow(EditState())

    val state = combine(_state, getGears()) { state, gears -> state.copy(gears = gears) }
        .onStart { if (autoLoad) load() }
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
        is EditAction.SetActivity -> _state.update {
            it.copy(
                activity = action.activity,
                stravaActivity = it.stravaActivities.find { a -> a.match(action.activity) },
                course = null,
                gear = null,
                eventType = null,
                name = null,
                description = null,
                water = null,
                effort = null,
                feel = null,
                trainingEffect = false,
            )
        }
        is EditAction.SetDescription -> _state.updateIf( { it.activity != null } )
            { it.copy(description = action.description) }
        is EditAction.SetName -> _state.updateIf( { it.activity != null } )
            { it.copy(name = action.name?.takeIf { n -> n.isNotEmpty() }) }
        is EditAction.SetEventType -> _state.updateIf( { it.activity != null } )
            { it.copy(eventType = action.eventType) }
        is EditAction.SetCourse ->  _state.updateIf( { it.activity != null && action.course != null && action.course.type.compatible(it.activity.type) })
            { it.copy( course = action.course ) }
        is EditAction.SetGear -> _state.updateIf( { it.activity != null && action.gear != null && action.gear.type.compatible(it.activity.type) } )
            { it.copy(gear = action.gear) }
        is EditAction.SetWater -> _state.updateIf( { it.activity != null } )
            { it.copy(water = action.water) }
        is EditAction.SetEffort -> _state.updateIf( { it.activity != null } )
            { it.copy(effort = action.effort?.takeIf { e -> e > 0 }) }
        is EditAction.SetFeel -> _state.updateIf( { it.activity != null } )
            { it.copy(feel = action.feel) }
        is EditAction.SetTrainingEffect -> _state.updateIf( { it.activity != null } )
            { it.copy(trainingEffect = action.trainingEffect) }
        is EditAction.Save -> saveAction()
        is EditAction.Restart -> restartAction()
    }

    private fun saveAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        val errors = mutableListOf<String>()

        val workout = state.value.activity?.workoutId?.let { getWorkout(it) }?.getOrNull()

        coroutineScope {
            val asyncUpdateActivity = async {
                updateActivity(
                    activity = state.value.activity,
                    name = state.value.name ?: state.value.activity?.name,
                    eventType = state.value.eventType,
                    course = state.value.course,
                    water = state.value.water,
                    feel = state.value.feel,
                    effort = state.value.effort,
                    workout = workout,
                    gear = state.value.gear,
                )
            }

            val asyncUpdateStravaActivity = async {
                state.value.stravaActivity?.let {
                    updateStravaActivity(
                        stravaActivity = it,
                        name = state.value.name ?: state.value.activity?.name,
                        description = state.value.description,
                        eventType = state.value.eventType,
                        trainingEffect = state.value.activity?.trainingEffect,
                        trainingEffectFlag = state.value.trainingEffect,
                        workout = workout
                    )
                }?: Result.success(Unit)
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