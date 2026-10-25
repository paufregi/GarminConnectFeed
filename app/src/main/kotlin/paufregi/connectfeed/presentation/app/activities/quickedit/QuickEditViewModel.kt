package paufregi.connectfeed.presentation.app.activities.quickedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetActivity
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.utils.finally
import paufregi.connectfeed.core.utils.updateIf
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.presentation.ui.models.Status

@ExperimentalCoroutinesApi
@HiltViewModel(assistedFactory = QuickEditViewModel.Factory::class)
class QuickEditViewModel @AssistedInject constructor(
    getProfiles: GetProfiles,
    getGears: GetGears,
    private val getActivity: GetActivity,
    private val getWorkout: GetWorkout,
    private val updateActivity: QuickUpdateActivity,
    private val notification: NotificationManager,
    @Assisted private val id: Long,
    @Assisted private val stravaId: Long?,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Long, stravaId: Long? = null): QuickEditViewModel
    }

    private val _state = MutableStateFlow(QuickEditState())

    val state = combine(_state, getProfiles(), getGears()) {
        state, profiles, gears -> state.copy(profiles = profiles, gears = gears)
    }
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(status = Status.Loading) }

        getActivity(id, stravaId)
            .onSuccess { activity -> _state.update { it.copy(activity = activity) } }
            .onFailure { error ->
                notification.show("Failed to load activity: ${error.message}")
                _state.update { it.copy(notFound = true) }
            }
            .finally { _state.update { it.copy(status = null) } }
    }

    internal fun injectState(state: QuickEditState) {
        _state.value = state
    }

    fun onAction(action: QuickEditAction) = when (action) {
        is QuickEditAction.SetProfile -> _state.updateIf({action.profile == null || action.profile.compatible(it.activity)}) { it.copy(profile = action.profile) }
        is QuickEditAction.SetGear -> _state.updateIf( { action.gear.compatible(it.activity) } ) { it.copy(gear = action.gear) }
        is QuickEditAction.SetDescription -> _state.update { it.copy(description = action.description) }
        is QuickEditAction.SetWater -> _state.update { it.copy(water = action.water) }
        is QuickEditAction.SetEffort -> _state.update { it.copy(effort = action.effort?.takeIf { e -> e > 0 }) }
        is QuickEditAction.SetFeel -> _state.update { it.copy(feel = action.feel) }
        is QuickEditAction.ResetStatus -> _state.update { it.copy(status = null) }
        is QuickEditAction.Save -> save()
    }

    private fun save() = viewModelScope.launch {
        if (state.value.notFound) return@launch

        _state.update { it.copy(status = Status.Loading) }

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
        .onSuccess { _state.update { it.copy(status = Status.Success()) } }
        .onFailure { r -> _state.update { it.copy(status = Status.Failure(r.message ?: "Something went wrong: unknown error")) } }
    }
}
