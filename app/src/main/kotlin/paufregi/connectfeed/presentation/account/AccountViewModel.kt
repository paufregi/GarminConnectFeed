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
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.ClearTokens
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    getUser: GetUser,
    val clearTokensUseCase: ClearTokens,
    val signOutUseCase: SignOut,
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())

    val state = combine(_state, getUser()) { state, user -> state.copy(user = user) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), AccountState())

    fun onEvent(event: AccountEvent) = when (event) {
        is AccountEvent.ClearTokens -> viewModelScope.launch { clearTokens() }
        is AccountEvent.SignOut -> viewModelScope.launch { signOut() }
        is AccountEvent.Reset -> _state.update { AccountState() }
    }

    private fun clearTokens() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        clearTokensUseCase()
        _state.update { AccountState(ProcessState.Success("Tokens cleared")) }
    }

    private fun signOut() = viewModelScope.launch {
        _state.update { AccountState(ProcessState.Processing) }
        signOutUseCase()
    }
}