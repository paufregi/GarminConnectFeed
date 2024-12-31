package paufregi.connectfeed.presentation.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.GetCredential
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.SignIn
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val getCredential: GetCredential,
    private val signInUseCase: SignIn,
) : ViewModel() {

    private val _state = MutableStateFlow(PasswordState())

    val state = _state
        .onStart { load() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500L), PasswordState())

    private fun load() = viewModelScope.launch {
        val credential = getCredential().firstOrNull() ?: Credential()
        _state.update { it.copy(credential = credential) }
    }

    fun onEvent(event: PasswordEvent) = when (event) {
            is PasswordEvent.SetPassword -> _state.update { it.copy(credential = it.credential.copy(password = event.password)) }
            is PasswordEvent.ShowPassword -> _state.update { it.copy(showPassword = event.showPassword) }
            is PasswordEvent.SignIn -> signIn()
        }

    private fun signIn() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        when (val res = signInUseCase(state.value.credential) ) {
            is Result.Success -> _state.update { it.copy(process = ProcessState.Success(res.data.name),) }
            is Result.Failure -> _state.update { it.copy(process = ProcessState.Failure(res.reason)) }
        }
    }
}