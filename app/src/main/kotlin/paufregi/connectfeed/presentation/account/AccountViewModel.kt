package paufregi.connectfeed.presentation.account

import android.content.Intent
import android.net.Uri
import android.app.Activity as AppActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.RefreshUser
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    getUser: GetUser,
    val refreshUserUseCase: RefreshUser,
    val signOutUseCase: SignOut,
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())

    val state = combine(_state, getUser()) { state, user -> state.copy(user = user) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), AccountState())

    fun onEvent(event: AccountEvent) = when (event) {
        is AccountEvent.RefreshUser -> refreshUser()
        is AccountEvent.SignOut -> signOut()
        is AccountEvent.Reset -> _state.update { AccountState() }
    }

    private fun refreshUser() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        refreshUserUseCase()
            .onSuccess { _state.update { AccountState(ProcessState.Success("User data refreshed")) } }
            .onFailure { err -> _state.update { AccountState(ProcessState.Failure(err)) } }
    }

    private fun signOut() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        signOutUseCase()
    }
}