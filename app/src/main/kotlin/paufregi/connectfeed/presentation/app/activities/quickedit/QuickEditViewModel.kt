package paufregi.connectfeed.presentation.app.activities.quickedit

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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.usecases.GetActivity
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.utils.finally
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager

@ExperimentalCoroutinesApi
@HiltViewModel(assistedFactory = QuickEditViewModel.Factory::class)
class QuickEditViewModel @AssistedInject constructor(
    getActivity: GetActivity,
    getProfiles: GetProfiles,
    val getGears: GetGears,
    val getWorkout: GetWorkout,
    val updateActivity: QuickUpdateActivity,
    val notification: NotificationManager,
    @Assisted private val id: Long,
    @Assisted private val stravaId: Long?,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Long, stravaId: Long? = null): QuickEditViewModel
    }

    private val _effects = Channel<QuickEditEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val _state = MutableStateFlow(
        QuickEditState(activity = getActivity(id, stravaId) ?: Activity.EMPTY)
    )

    val state = combine(_state, getProfiles(), getGears()) {
        state, profiles, gears -> state.copy(profiles = profiles, gears = gears)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    init {
        if (_state.value.activity == Activity.EMPTY) {
            notification.show("Activity not found")
            navigateBack()
        }
    }

    fun onAction(action: QuickEditAction) = when (action) {
        is QuickEditAction.SetProfile -> _state.update { it.copy(profile = action.profile) }
        is QuickEditAction.SetGear -> _state.update { it.copy(gear = action.gear) }
        is QuickEditAction.SetDescription -> _state.update { it.copy(description = action.description) }
        is QuickEditAction.SetWater -> _state.update { it.copy(water = action.water) }
        is QuickEditAction.SetEffort -> _state.update { it.copy(effort = action.effort?.takeIf { e -> e > 0 }) }
        is QuickEditAction.SetFeel -> _state.update { it.copy(feel = action.feel) }
        is QuickEditAction.Save -> save()
    }

    private fun save() = viewModelScope.launch {
        if (state.value.activity == Activity.EMPTY) return@launch

        _state.update { it.copy(loading = true) }

        val workout = state.value.activity.workoutId?.let { getWorkout(it) }?.getOrNull()

        updateActivity(
            activity = state.value.activity,
            profile = state.value.profile,
            description = state.value.description,
            water = state.value.water,
            feel = state.value.feel,
            effort = state.value.effort,
            workout = workout,
            gear = state.value.gear,
        )
        .onSuccess {
            notification.show("Activity updated")
            navigateBack()
        }
        .onFailure { notification.show(it.message ?: "Error") }
        .finally { _state.update { it.copy(loading = false) } }
    }

    private fun navigateBack() = viewModelScope.launch {
        _effects.send(QuickEditEffect.NavigateBack)
    }
}

