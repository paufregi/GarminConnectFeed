package paufregi.connectfeed.presentation.app.activities.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.usecases.GetActivity
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.utils.finally
import paufregi.connectfeed.presentation.ui.utils.SnackbarManager

@ExperimentalCoroutinesApi
@HiltViewModel(assistedFactory = ActivityViewModel.Factory::class)
class ActivityViewModel @AssistedInject constructor(
    getActivity: GetActivity,
    getEventTypes: GetEventTypes,
    val getCourses: GetCourses,
    val getWorkout: GetWorkout,
    val updateActivity: UpdateActivity,
    val notificationManager: SnackbarManager,
    @Assisted private val id: Long,
    @Assisted private val stravaId: Long?,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Long, stravaId: Long? = null): ActivityViewModel
    }

    private val _effects = Channel<ActivityEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val _state = MutableStateFlow(
        ActivityState(
            activity = getActivity(id, stravaId) ?: Activity.EMPTY,
            eventTypes = getEventTypes()
        )
    )

    init {
        if (_state.value.activity == Activity.EMPTY) {
            notificationManager.showMessage("Activity not found")
            navigateBack()
        }
    }

    val state = _state
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    private fun load() = viewModelScope.launch{
        if(_state.value.activity.type.allowCourse) {
            _state.update { it.copy(loading = true) }

            getCourses()
                .onSuccess { courses -> _state.update { it.copy(courses = courses) } }
                .onFailure { notificationManager.showMessage("Failed to load courses: ${it.message}") }
                .finally { _state.update { it.copy(loading = false) } }
        }
    }

    fun onAction(action: ActivityAction) = when (action) {
        is ActivityAction.SetName -> _state.update { it.copy(name = action.name) }
        is ActivityAction.SetDescription -> _state.update { it.copy(description = action.description) }
        is ActivityAction.SetEventType -> _state.update { it.copy(eventType = action.eventType) }
        is ActivityAction.SetCourse ->  _state.update { it.copy( course = action.course ) }
        is ActivityAction.SetGear -> _state.update { it.copy(gear = action.gear) }
        is ActivityAction.SetWater -> _state.update { it.copy(water = action.water) }
        is ActivityAction.SetEffort -> _state.update { it.copy(effort = action.effort?.takeIf { e -> e > 0 }) }
        is ActivityAction.SetFeel -> _state.update { it.copy(feel = action.feel) }
        is ActivityAction.SetTrainingEffect -> _state.update { it.copy(trainingEffect = action.trainingEffect) }
        is ActivityAction.Save -> save()
        is ActivityAction.Cancel -> navigateBack()
    }

    private fun save() = viewModelScope.launch {
        if (state.value.activity == Activity.EMPTY) return@launch

        _state.update { it.copy(loading = true) }

        val workout = state.value.activity.workoutId?.let { getWorkout(it) }?.getOrNull()

        updateActivity(
            activity = state.value.activity,
            name = state.value.name ?: state.value.activity.name,
            description = state.value.description,
            eventType = state.value.eventType,
            course = state.value.course,
            water = state.value.water,
            feel = state.value.feel,
            effort = state.value.effort,
            workout = workout,
            gear = state.value.gear,
            trainingEffect = state.value.trainingEffect,
        )
        .onSuccess {
            notificationManager.showMessage("Activity updated")
            navigateBack()
        }
        .onFailure { notificationManager.showMessage(it.message ?: "Error") }
        .finally { _state.update { it.copy(loading = false) } }
    }

    private fun navigateBack() = viewModelScope.launch {
        _effects.send(ActivityEffect.NavigateBack)
    }
}

