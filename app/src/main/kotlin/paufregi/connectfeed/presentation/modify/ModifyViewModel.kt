package paufregi.connectfeed.presentation.modify

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.usecases.QuickUpdateStravaActivity
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.core.utils.updateIf
import paufregi.connectfeed.presentation.ui.models.ProcessState

@HiltViewModel
@ExperimentalCoroutinesApi
class ModifyViewModel @Inject constructor(
    val getActivities: GetActivities,
    val getStravaActivities: GetStravaActivities,
    val getProfiles: GetProfiles,
    val getGears: GetGears,
    val getEventTypes: GetEventTypes,
    val getCourses: GetCourses,
    val getWorkout: GetWorkout,
    val updateActivity: UpdateActivity,
    val updateStravaActivity: UpdateStravaActivity,
    val quickUpdateActivity: QuickUpdateActivity,
    val quickUpdateStravaActivity: QuickUpdateStravaActivity,
) : ViewModel() {

    @VisibleForTesting
    internal fun seedStateForTest(state: ModifyState) {
        _state.value = state
        autoLoad = false
    }

    private var autoLoad = true

    private val _state = MutableStateFlow(ModifyState())

    val state = combine(_state, getProfiles(), getGears()) { state, profiles, gears ->
        state.copy(profiles = profiles, gears = gears)
    }
        .onStart { if (autoLoad) load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ModifyState())

    private fun load(force: Boolean = false) = viewModelScope.launch {
        _state.update {
            it.copy(
                process = ProcessState.Processing,
                eventTypes = getEventTypes(),
            )
        }

        val errors = mutableListOf<String>()
        val activityErrors = mutableListOf<String>()
        val courseErrors = mutableListOf<String>()

        coroutineScope {
            val asyncGetActivities = async { getActivities(force) }
            val asyncGetStravaActivities = async { getStravaActivities(force) }
            val asyncGetCourses = async { getCourses(force) }

            runCatchingResult { asyncGetActivities.await() }
                .onSuccess { data -> _state.update { it.copy(activities = data) } }
                .onFailure { activityErrors.add("Garmin") }

            runCatchingResult { asyncGetStravaActivities.await() }
                .onSuccess { data -> _state.update { it.copy(stravaActivities = data) } }
                .onFailure { activityErrors.add("Strava") }

            runCatchingResult { asyncGetCourses.await() }
                .onSuccess { data -> _state.update { it.copy(courses = data) } }
                .onFailure { courseErrors.add("courses") }
        }

        if (activityErrors.isNotEmpty()) {
            errors.add("${activityErrors.joinToString(" & ")} activities")
        }
        errors.addAll(courseErrors)

        _state.update {
            it.copy(
                process = if (errors.isEmpty()) {
                    ProcessState.Idle
                } else {
                    ProcessState.Failure("Couldn't load ${errors.joinToString(" & ")}")
                }
            )
        }
    }

    fun onAction(action: ModifyAction) = when (action) {
        is ModifyAction.SetActivity -> _state.update {
            it.resetEdits().copy(
                activity = action.activity,
                stravaActivity = it.stravaActivities.find { a -> a.match(action.activity) },
            )
        }
        is ModifyAction.ClearActivity -> _state.update { it.resetEdits() }
        is ModifyAction.SetMode -> _state.updateIf({ it.activity != null }) { state ->
            state.resetEdits().copy(
                activity = state.activity,
                stravaActivity = state.stravaActivity,
                mode = action.mode,
            )
        }
        is ModifyAction.SetName -> _state.updateIf({ it.activity != null && it.mode == ModifyMode.Manual }) {
            it.copy(name = action.name?.takeIf { value -> value.isNotEmpty() })
        }
        is ModifyAction.SetEventType -> _state.updateIf({ it.activity != null && it.mode == ModifyMode.Manual }) {
            it.copy(eventType = action.eventType)
        }
        is ModifyAction.SetCourse -> _state.updateIf({
            it.activity != null &&
                it.mode == ModifyMode.Manual &&
                it.activity.type.allowCourse &&
                action.course != null &&
                action.course.type.compatible(it.activity.type)
        }) {
            it.copy(course = action.course)
        }
        is ModifyAction.SetProfile -> _state.updateIf({
            it.activity != null &&
                it.mode == ModifyMode.Profile &&
                action.profile.type.compatible(it.activity.type)
        }) {
            it.copy(profile = action.profile, water = action.profile.water, gear = null, effort = null, feel = null)
        }
        is ModifyAction.SetGear -> _state.updateIf({
            it.activity != null &&
                action.gear != null &&
                action.gear.type.compatible(it.activity.type) &&
                (it.mode == ModifyMode.Manual || (it.mode == ModifyMode.Profile && it.profile != null))
        }) {
            it.copy(gear = action.gear)
        }
        is ModifyAction.SetDescription -> _state.updateIf({
            it.activity != null && (it.mode == ModifyMode.Manual || (it.mode == ModifyMode.Profile && it.profile != null))
        }) {
            it.copy(description = action.description)
        }
        is ModifyAction.SetWater -> _state.updateIf({
            it.activity != null && when (it.mode) {
                ModifyMode.Manual -> true
                ModifyMode.Profile -> it.profile?.customWater == true
            }
        }) {
            it.copy(water = action.water)
        }
        is ModifyAction.SetEffort -> _state.updateIf({
            it.activity != null && when (it.mode) {
                ModifyMode.Manual -> true
                ModifyMode.Profile -> it.profile?.feelAndEffort == true
            }
        }) {
            it.copy(effort = if (it.mode == ModifyMode.Manual) action.effort?.takeIf { value -> value > 0 } else action.effort)
        }
        is ModifyAction.SetFeel -> _state.updateIf({
            it.activity != null && when (it.mode) {
                ModifyMode.Manual -> true
                ModifyMode.Profile -> it.profile?.feelAndEffort == true
            }
        }) {
            it.copy(feel = action.feel)
        }
        is ModifyAction.SetTrainingEffect -> _state.updateIf({ it.activity != null && it.mode == ModifyMode.Manual }) {
            it.copy(trainingEffect = action.trainingEffect)
        }
        is ModifyAction.Save -> when (state.value.mode) {
            ModifyMode.Manual -> saveManualAction()
            ModifyMode.Profile -> saveProfileAction()
        }
        is ModifyAction.Restart -> restartAction()
    }

    private fun saveManualAction() = viewModelScope.launch {
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
                        workout = workout,
                    )
                } ?: Result.success(Unit)
            }

            runCatchingResult { asyncUpdateActivity.await() }
                .onFailure { errors.add("Garmin") }

            runCatchingResult { asyncUpdateStravaActivity.await() }
                .onFailure { errors.add("Strava") }
        }

        _state.update {
            it.copy(
                process = if (errors.isEmpty()) {
                    ProcessState.Success("Activity updated")
                } else {
                    ProcessState.Failure("Couldn't update ${errors.joinToString(" & ")} activity")
                }
            )
        }
    }

    private fun saveProfileAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        val errors = mutableListOf<String>()

        val workout = state.value.activity?.workoutId?.let { getWorkout(it) }?.getOrNull()

        coroutineScope {
            val asyncQuickUpdate = async {
                quickUpdateActivity(
                    activity = state.value.activity,
                    profile = state.value.profile,
                    water = state.value.water,
                    feel = state.value.feel,
                    effort = state.value.effort,
                    workout = workout,
                    gear = state.value.gear,
                )
            }

            val asyncQuickUpdateStrava = async {
                state.value.stravaActivity?.let {
                    quickUpdateStravaActivity(
                        activity = state.value.activity,
                        stravaActivity = it,
                        profile = state.value.profile,
                        description = state.value.description,
                        workout = workout,
                    )
                } ?: Result.success(Unit)
            }

            runCatchingResult { asyncQuickUpdate.await() }
                .onFailure { errors.add("Garmin") }

            runCatchingResult { asyncQuickUpdateStrava.await() }
                .onFailure { errors.add("Strava") }
        }

        _state.update {
            it.copy(
                process = if (errors.isEmpty()) {
                    ProcessState.Success("Activity updated")
                } else {
                    ProcessState.Failure("Couldn't update ${errors.joinToString(" & ")} activity")
                }
            )
        }
    }

    private fun restartAction() {
        _state.update { ModifyState() }
        load(true)
    }

    private fun ModifyState.resetEdits(): ModifyState = copy(
        process = ProcessState.Idle,
        activity = null,
        stravaActivity = null,
        mode = ModifyMode.Manual,
        name = null,
        eventType = null,
        course = null,
        profile = null,
        gear = null,
        description = null,
        water = null,
        effort = null,
        feel = null,
        trainingEffect = false,
    )
}
