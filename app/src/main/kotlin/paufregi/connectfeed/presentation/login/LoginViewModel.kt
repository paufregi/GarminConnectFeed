package paufregi.connectfeed.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.SignIn
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signIn: SignIn,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())

    val state = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500L), LoginState())

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.SetUsername -> _state.update { it.copy(username = action.username) }
            is LoginAction.SetPassword -> _state.update { it.copy(password = action.password) }
            is LoginAction.ShowPassword -> _state.update { it.copy(showPassword = action.showPassword) }
            is LoginAction.Reset -> _state.update { LoginState() }
            is LoginAction.SignIn -> signInAction()
        }
    }

    private fun signInAction() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        signIn(state.value.username, state.value.password)
            .onSuccess { data -> _state.update { it.copy(process = ProcessState.Success(), user = data) } }
            .onFailure { err -> _state.update { it.copy(process = ProcessState.Failure(err.message ?: "Error")) } }
    }
}