package paufregi.connectfeed.presentation.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.usecases.SaveUser
import paufregi.connectfeed.core.usecases.SignIn
import paufregi.connectfeed.presentation.ui.models.Status
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signIn: SignIn,
    private val saveUser: SaveUser,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())

    val state = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500L), _state.value)

    internal fun injectState(state: LoginState) {
        _state.value = state
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.SetUsername -> _state.update { it.copy(username = action.username) }
            is LoginAction.SetPassword -> _state.update { it.copy(password = action.password) }
            is LoginAction.ShowPassword -> _state.update { it.copy(showPassword = action.showPassword) }
            is LoginAction.Reset -> _state.update { LoginState() }
            is LoginAction.SignIn -> signInAction()
            is LoginAction.SaveUser -> saveUserAction()
        }
    }

    private fun signInAction() = viewModelScope.launch {
        _state.update { it.copy(status = Status.Loading) }
        signIn(state.value.username, state.value.password)
            .onSuccess { user -> _state.update { it.copy(status = Status.Success(), user = user) } }
            .onFailure { r -> _state.update { it.copy(status = Status.Failure(r.message ?: "Something went wrong: unknown error")) } }
    }

    private fun saveUserAction() = viewModelScope.launch {
        state.value.user?.let { saveUser(it) }
    }
}