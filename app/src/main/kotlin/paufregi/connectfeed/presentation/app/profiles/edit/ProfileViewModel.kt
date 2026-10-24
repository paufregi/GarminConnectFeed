package paufregi.connectfeed.presentation.app.profiles.edit

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
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.GetActivityTypesForProfile
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetProfile
import paufregi.connectfeed.core.usecases.SaveProfile
import paufregi.connectfeed.core.utils.finally
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager

@ExperimentalCoroutinesApi
@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
class ProfileViewModel @AssistedInject constructor(
    getActivityTypes: GetActivityTypesForProfile,
    getEventTypes: GetEventTypes,
    private val getProfile: GetProfile,
    private val getCourses: GetCourses,
    private val saveProfile: SaveProfile,
    private val notification: NotificationManager,
    @Assisted private val id: Long?,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Long?): ProfileViewModel
    }

    private val _effects = Channel<ProfileEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val _state = MutableStateFlow(
        ProfileState(
            activityTypes = getActivityTypes(),
            eventTypes = getEventTypes()
        )
    )

    private var loaded = false

    val state = _state
        .onStart { if (!loaded) load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    internal fun injectState(state: ProfileState) {
        _state.value = state
    }

    private fun load() = viewModelScope.launch {
        loaded = true
        _state.update { it.copy(
            loading = true,
            profile = getProfile(id) ?: Profile()
        ) }

        getCourses()
            .onSuccess { courses -> _state.update { it.copy(courses = courses) } }
            .finally { _state.update { it.copy(loading = false) } }
    }

    fun onAction(action: ProfileAction) = when (action) {
        is ProfileAction.SetName -> _state.update { it.copy(profile = it.profile.copy(name = action.value)) }
        is ProfileAction.SetType -> _state.update { it.copy(
            profile = it.profile.copy(
                type = action.value,
                course = it.profile.course?.takeIf { c -> action.value.compatible(c.type) }
            )
        ) }
        is ProfileAction.SetEventType -> _state.update { it.copy(profile = it.profile.copy(eventType = action.value)) }
        is ProfileAction.SetCourse -> _state.update { it.copy(profile = it.profile.copy(course = action.value)) }
        is ProfileAction.SetWater -> _state.update { it.copy(
            profile = it.profile.copy(water = action.value.filter(Char::isDigit).toIntOrNull())
        ) }
        is ProfileAction.SetRename -> _state.update { it.copy(profile = it.profile.copy(rename = action.value)) }
        is ProfileAction.SetCustomWater -> _state.update { it.copy(profile = it.profile.copy(customWater = action.value)) }
        is ProfileAction.SetGear -> _state.update { it.copy(profile = it.profile.copy(gear = action.value)) }
        is ProfileAction.SetFeelAndEffort -> _state.update { it.copy(profile = it.profile.copy(feelAndEffort = action.value)) }
        is ProfileAction.SetTrainingEffect -> _state.update { it.copy(profile = it.profile.copy(trainingEffect = action.value)) }
        is ProfileAction.Save -> save()
    }

    private fun save() = viewModelScope.launch {
        _state.update { it.copy(loading = true) }
        saveProfile(state.value.profile)
            .onSuccess {
                notification.show("Profile saved")
                navigateBack()
            }
            .onFailure { notification.show(it.message ?: "Error") }
            .finally { _state.update { it.copy(loading = false) } }
    }

    private fun navigateBack() = viewModelScope.launch {
        _effects.send(ProfileEffect.NavigateBack)
    }
}
