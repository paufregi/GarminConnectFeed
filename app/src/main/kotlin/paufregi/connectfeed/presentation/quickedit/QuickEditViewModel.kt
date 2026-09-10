package paufregi.connectfeed.presentation.quickedit

import android.util.Log
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
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.usecases.QuickUpdateStravaActivity
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.core.utils.updateIf
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class QuickEditViewModel @Inject constructor(
    val getActivities: GetActivities,
    val getStravaActivities: GetStravaActivities,
    val getProfiles: GetProfiles,
    val getGears: GetGears,
    val quickUpdateActivity: QuickUpdateActivity,
    val quickUpdateStravaActivity: QuickUpdateStravaActivity,
    val getWorkout: GetWorkout
) : ViewModel() {

    @VisibleForTesting
    internal fun seedStateForTest(state: QuickEditState) {
        _state.value = state
        autoLoad = false
    }

    private val _state = MutableStateFlow(QuickEditState())
        private var autoLoad = true

    val state = combine(_state, getProfiles(), getGears()) {
            state, profiles, gears -> state.copy(profiles = profiles, gears = gears)
    }
        .onStart { if (autoLoad) load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), QuickEditState())

    private fun load(force: Boolean = false) = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        val errors = mutableListOf<String>()

        coroutineScope {
            val asyncGetActivities = async { getActivities(force) }
            val asyncGetStravaActivities = async { getStravaActivities(force) }

            runCatchingResult { asyncGetActivities.await() }
                .onSuccess { data -> _state.update { it.copy(activities = data) } }
                .onFailure { errors.add("activities") }

            runCatchingResult { asyncGetStravaActivities.await() }
                .onSuccess { data -> _state.update { it.copy(stravaActivities = data) } }
                .onFailure { errors.add("Strava activities") }
        }

        when (errors.isEmpty()) {
            true -> _state.update { it.copy(process = ProcessState.Idle) }
            false -> _state.update { it.copy(process = ProcessState.Failure("Couldn't load ${errors.joinToString(" & ")}")) }
        }
    }

    fun onAction(action: QuickEditAction) = when (action) {
        is QuickEditAction.SetActivity -> _state.update {
            it.copy(
                activity = action.activity,
                stravaActivity = it.stravaActivities.find { a -> a.match(action.activity) },
                profile = null,
                gear = null,
                water = null,
                effort = null,
                feel = null
            )
        }
        is QuickEditAction.SetProfile -> _state.updateIf(
            { it.activity != null && action.profile.type.compatible(it.activity.type) }
        ) { it.copy(profile = action.profile, water = action.profile.water) }
        is QuickEditAction.SetGear -> _state.updateIf(
            { it.activity != null && it.profile != null && action.gear.type.compatible(it.activity.type) }
        ) { it.copy(gear = action.gear) }
        is QuickEditAction.SetDescription -> _state.updateIf(
            { it.activity != null && it.profile != null }
        ) { it.copy(description = action.description) }
        is QuickEditAction.SetWater -> _state.updateIf(
            { it.activity != null && it.profile != null && it.profile.customWater }
        ) { it.copy(water = action.water) }
        is QuickEditAction.SetEffort -> _state.updateIf(
            { it.activity != null && it.profile != null && it.profile.feelAndEffort }
        ) { it.copy(effort = action.effort) }
        is QuickEditAction.SetFeel -> _state.updateIf(
            { it.activity != null && it.profile != null && it.profile.feelAndEffort }
        ) { it.copy(feel = action.feel) }
        is QuickEditAction.Save -> saveAction()
        is QuickEditAction.Restart -> restartAction()
    }

    private fun saveAction() = viewModelScope.launch {
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
                        workout = workout
                    )
                } ?:  Result.success(Unit)
            }

            runCatchingResult { asyncQuickUpdate.await() }
                .onFailure { errors.add("Garmin") }

            runCatchingResult { asyncQuickUpdateStrava.await() }
                .onFailure { errors.add("Strava") }
        }

        when (errors.isEmpty()) {
            true -> _state.update { it.copy(process = ProcessState.Success("Activity updated")) }
            false -> _state.update {
                it.copy(process = ProcessState.Failure("Couldn't update ${errors.joinToString(" & ")} activity"))
            }
        }
    }

    private fun restartAction() {
        _state.update { QuickEditState() }
        load(true)
    }
}