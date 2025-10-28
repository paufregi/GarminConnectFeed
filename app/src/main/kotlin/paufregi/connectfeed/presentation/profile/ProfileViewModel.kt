package paufregi.connectfeed.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.GetActivityCategories
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetProfile
import paufregi.connectfeed.core.usecases.SaveProfile
import paufregi.connectfeed.core.utils.takeIfCompatible
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getProfile: GetProfile,
    val getActivityCategories: GetActivityCategories,
    val getEventTypes: GetEventTypes,
    val getCourses: GetCourses,
    val saveProfile: SaveProfile,
) : ViewModel() {

    private val profileId: Long = savedStateHandle.toRoute<Route.Profile>().id

    private val _state = MutableStateFlow(ProfileState())

    val state = _state
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), ProfileState())

    private fun load() = viewModelScope.launch {
        _state.update {
            it.copy(
                process = ProcessState.Processing,
                profile = getProfile(profileId) ?: Profile(),
                categories = getActivityCategories(),
                eventTypes = getEventTypes()
            )
        }

        getCourses()
            .onSuccess { data -> _state.update { it.copy(courses = data, process = ProcessState.Idle) } }
            .onFailure { _state.update { it.copy(process = ProcessState.Failure("Couldn't load courses")) } }
    }

    fun onAction(action: ProfileAction) = when (action) {
        is ProfileAction.SetName -> _state.update { it.copy(profile = it.profile.copy(name = action.name)) }
        is ProfileAction.SetCategory -> _state.update { it.copy(
            profile = it.profile.copy(
                category = action.category,
                course = it.profile.course.takeIfCompatible(action.category)
            )
        ) }
        is ProfileAction.SetEventType -> _state.update { it.copy(profile = it.profile.copy(eventType = action.eventType)) }
        is ProfileAction.SetCourse -> _state.update { it.copy(
            profile = it.profile.copy(
                course = action.course.takeIfCompatible(it.profile.category),
            )
        ) }
        is ProfileAction.SetWater -> _state.update { it.copy(profile = it.profile.copy(water = action.water)) }
        is ProfileAction.SetRename -> _state.update { it.copy(profile = it.profile.copy(rename = action.rename)) }
        is ProfileAction.SetCustomWater -> _state.update { it.copy(profile = it.profile.copy(customWater = action.customWater)) }
        is ProfileAction.SetFeelAndEffort -> _state.update { it.copy(profile = it.profile.copy(feelAndEffort = action.feelAndEffort)) }
        is ProfileAction.SetTrainingEffect -> _state.update { it.copy(profile = it.profile.copy(trainingEffect = action.trainingEffect)) }
        is ProfileAction.Save -> saveAction()
    }

    private fun saveAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        saveProfile(state.value.profile)
            .onSuccess { _state.update { it.copy(process = ProcessState.Success("Profile saved")) }}
            .onFailure { err -> _state.update { it.copy(process = ProcessState.Failure(err.message ?: "Error")) }}
    }
}
