package paufregi.connectfeed.presentation.quickedit

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
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.usecases.QuickUpdateStravaActivity
import paufregi.connectfeed.core.utils.getOrMatch
import paufregi.connectfeed.core.utils.getOrNull
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class QuickEditViewModel @Inject constructor(
    val getLatestActivities: GetLatestActivities,
    val getLatestStravaActivities: GetLatestStravaActivities,
    getProfiles: GetProfiles,
    val quickUpdateActivity: QuickUpdateActivity,
    val quickUpdateStravaActivity: QuickUpdateStravaActivity
) : ViewModel() {

    private val _state = MutableStateFlow(QuickEditState())

    val state =
        combine(_state, getProfiles()) { state, profiles -> state.copy(profiles = profiles) }
            .onStart { load() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), QuickEditState())

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

    fun onAction(action: QuickEditAction) = when (action) {
        is QuickEditAction.SetActivity -> _state.update {
            it.copy(
                activity = action.activity,
                profile = it.profile.getOrNull(action.activity),
                stravaActivity = it.stravaActivity.getOrMatch(action.activity, it.stravaActivities),
            )
        }
        is QuickEditAction.SetStravaActivity -> _state.update {
            it.copy(
                stravaActivity = action.activity,
                profile = it.profile.getOrNull(action.activity),
                activity = it.activity.getOrMatch(action.activity, it.activities),
            )
        }
        is QuickEditAction.SetProfile -> _state.update { it.copy(
            profile = action.profile,
            activity = it.activity.getOrNull(action.profile),
            stravaActivity = it.stravaActivity.getOrNull(action.profile),
        ) }
        is QuickEditAction.SetDescription -> _state.update { it.copy(description = action.description) }
        is QuickEditAction.SetWater -> _state.update { it.copy(profile = it.profile?.copy(water = action.water)) }
        is QuickEditAction.SetEffort -> _state.update { it.copy(effort = action.effort.getOrNull()) }
        is QuickEditAction.SetFeel -> _state.update { it.copy(feel = action.feel) }
        is QuickEditAction.Save -> saveAction()
        is QuickEditAction.Restart -> restartAction()
    }

    private fun saveAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        val errors = mutableListOf<String>()
        quickUpdateActivity(
            activity = state.value.activity,
            profile = state.value.profile,
            feel = state.value.feel,
            effort = state.value.effort
        ).onFailure { errors.add("activity") }

        if (state.value.hasStrava && state.value.stravaActivity != null) {
            quickUpdateStravaActivity(
                activity = state.value.activity,
                stravaActivity = state.value.stravaActivity,
                profile = state.value.profile,
                description = state.value.description,
            ).onFailure { errors.add("Strava activity") }
        }

        when (errors.isNotEmpty()) {
            false -> _state.update { it.copy(process = ProcessState.Success("Activity updated")) }
            true -> _state.update {
                it.copy(process = ProcessState.Failure("Couldn't update ${errors.joinToString(" & ")}"))
            }
        }
    }

    private fun restartAction() {
        _state.update { QuickEditState() }
        load()
    }
}