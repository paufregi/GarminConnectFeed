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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.GetActivityTypes
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetProfile
import paufregi.connectfeed.core.usecases.SaveProfile
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getProfile: GetProfile,
    val getActivityTypes: GetActivityTypes,
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
                activityTypes = getActivityTypes(),
                eventTypes = getEventTypes()
            )
        }

        getCourses()
            .onSuccess { data -> _state.update { it.copy(courses = data, process = ProcessState.Idle) } }
            .onFailure { _state.update { it.copy(process = ProcessState.Failure("Couldn't load courses")) } }

    }

    fun onAction(event: ProfileAction) = when (event) {
        is ProfileAction.SetName -> _state.update { it.copy(profile = it.profile.copy(name = event.name)) }
        is ProfileAction.SetActivityType -> _state.update {
            it.copy(profile = it.profile.copy(
                activityType = event.activityType,
                course = if (event.activityType == it.profile.course?.type) it.profile.course else null,
            ))
        }

        is ProfileAction.SetEventType -> _state.update { it.copy(profile = it.profile.copy(eventType = event.eventType)) }
        is ProfileAction.SetCourse -> _state.update { it.copy(profile = it.profile.copy(course = event.course)) }
        is ProfileAction.SetWater -> _state.update { it.copy(profile = it.profile.copy(water = event.water)) }
        is ProfileAction.SetRename -> _state.update { it.copy(profile = it.profile.copy(rename = event.rename)) }
        is ProfileAction.SetCustomWater -> _state.update { it.copy(profile = it.profile.copy(customWater = event.customWater)) }
        is ProfileAction.SetFeelAndEffort -> _state.update { it.copy(profile = it.profile.copy(feelAndEffort = event.feelAndEffort)) }
        is ProfileAction.SetTrainingEffect -> _state.update { it.copy(profile = it.profile.copy(trainingEffect = event.trainingEffect)) }
        is ProfileAction.Save -> save()
    }

    private fun save() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        when (val res = saveProfile(state.value.profile)) {
            is Result.Success -> _state.update { it.copy(process = ProcessState.Success("Profile saved")) }
            is Result.Failure -> _state.update { it.copy(process = ProcessState.Failure(res.reason)) }
        }
    }
}