package paufregi.connectfeed.presentation.app.profiles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.DeleteProfile
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.presentation.ui.utils.SnackbarManager
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class ProfilesViewModel @Inject constructor(
    getProfiles: GetProfiles,
    val deleteProfile: DeleteProfile,
    val notificationManager: SnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfilesState())

    val state = combine(_state, getProfiles()) { state, profiles -> state.copy(profiles = profiles) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), _state.value)

    fun onAction(action: ProfilesAction) {
        when (action) {
            is ProfilesAction.Delete -> deleteAction(action.profile)
        }
    }

    private fun deleteAction(profile: Profile) = viewModelScope.launch {
        deleteProfile(profile)
        notificationManager.showMessage("Profile deleted")
    }
}

