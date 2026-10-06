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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.usecases.GetActivity
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.utils.finally
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager

@ExperimentalCoroutinesApi
@HiltViewModel(assistedFactory = EditViewModel.Factory::class)
class EditViewModel @AssistedInject constructor(
    getActivity: GetActivity,
    getEventTypes: GetEventTypes,
    val getCourses: GetCourses,
    val getGears: GetGears,
    val getWorkout: GetWorkout,
    val updateActivity: UpdateActivity,
    val notification: NotificationManager,
    @Assisted private val id: Long,
    @Assisted private val stravaId: Long?,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Long, stravaId: Long? = null): EditViewModel
    }

    private val _effects = Channel<EditEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val _state = MutableStateFlow(
        EditState(
            activity = getActivity(id, stravaId) ?: Activity.EMPTY,
            eventTypes = getEventTypes()
        )
    )

    val state = combine(_state, getGears()) { state, gears -> state.copy(gears = gears) }
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    init {
        if (_state.value.activity == Activity.EMPTY) {
            notification.show("Activity not found")
            navigateBack()
        }
    }

    private fun load() = viewModelScope.launch{
        _state.update { it.copy(loading = true) }
        getCourses()
            .onSuccess { courses -> _state.update { it.copy(courses = courses) } }
            .onFailure { notification.show("Failed to load courses: ${it.message}") }
            .finally { _state.update { it.copy(loading = false) } }
    }

    fun onAction(action: EditAction) = when (action) {
        is EditAction.SetName -> _state.update { it.copy(name = action.name) }
        is EditAction.SetDescription -> _state.update { it.copy(description = action.description) }
        is EditAction.SetEventType -> _state.update { it.copy(eventType = action.eventType) }
        is EditAction.SetCourse ->  _state.update { it.copy( course = action.course ) }
        is EditAction.SetGear -> _state.update { it.copy(gear = action.gear) }
        is EditAction.SetWater -> _state.update { it.copy(water = action.water) }
        is EditAction.SetEffort -> _state.update { it.copy(effort = action.effort?.takeIf { e -> e > 0 }) }
        is EditAction.SetFeel -> _state.update { it.copy(feel = action.feel) }
        is EditAction.SetTrainingEffect -> _state.update { it.copy(trainingEffect = action.trainingEffect) }
        is EditAction.Save -> save()
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
            notification.show("Activity updated")
            navigateBack()
        }
        .onFailure { notification.show(it.message ?: "Error") }
        .finally { _state.update { it.copy(loading = false) } }
    }

    private fun navigateBack() = viewModelScope.launch {
        _effects.send(EditEffect.NavigateBack)
    }
}

