package paufregi.connectfeed.presentation.setup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.SignIn
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val signInUseCase: SignIn,
) : ViewModel() {

    private val _state = MutableStateFlow(SetupState())

    val state = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SetupState())

    fun onEvent(event: SetupEvent) {
        when (event) {
            is SetupEvent.SetUsername -> _state.change(username = event.username)
            is SetupEvent.SetPassword -> _state.change(password = event.password)
            is SetupEvent.ShowPassword -> _state.change(showPassword = event.showPassword)
            is SetupEvent.Reset -> _state.update { SetupState() }
            is SetupEvent.Save -> signIn()
        }
    }

    private fun signIn() = viewModelScope.launch {
        _state.change(processState = ProcessState.Processing)
        Log.i("ConnectFeed", "signIn")
        when (val res = signInUseCase(state.value.credential) ) {
            is Result.Success -> _state.change(processState = ProcessState.Success(res.data))
            is Result.Failure -> _state.change(processState = ProcessState.Failure(res.reason))
        }
    }
}