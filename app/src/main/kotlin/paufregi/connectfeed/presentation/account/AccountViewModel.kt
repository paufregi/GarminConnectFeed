package paufregi.connectfeed.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.ChangePassword
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.RefreshTokens
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    getUser: GetUser,
    val signOutUseCase: SignOut,
    val changePasswordUseCase: ChangePassword,
    val refreshTokenUseCase: RefreshTokens,
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())

    val state = combine(_state, getUser()) { state, user -> state.copy(user = user) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), AccountState())

    fun onEvent(event: AccountEvent) = when (event) {
        is AccountEvent.ChangePassword -> viewModelScope.launch { changePassword() }
        is AccountEvent.RefreshTokens -> viewModelScope.launch { refreshToken() }
        is AccountEvent.SignOut -> viewModelScope.launch { signOut() }
        is AccountEvent.Reset -> _state.update { AccountState() }
    }

    private fun changePassword() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        changePasswordUseCase(state.value.password)
        _state.update { AccountState(ProcessState.Success("Password updated")) }
    }

    private fun signOut() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        signOutUseCase()
    }

    private fun refreshToken() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        when (val res = refreshTokenUseCase()) {
            is Result.Failure -> _state.update { AccountState(ProcessState.Failure(res.reason)) }
            is Result.Success -> _state.update { AccountState(ProcessState.Success("Token refreshed")) }
        }
    }
}