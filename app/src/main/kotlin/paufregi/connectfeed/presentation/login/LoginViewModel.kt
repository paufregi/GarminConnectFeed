package paufregi.connectfeed.presentation.login

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.SignIn
import paufregi.connectfeed.presentation.ui.models.ProcessState
import javax.inject.Inject

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignIn,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())

    val state = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500L), LoginState())

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SetUsername -> _state.update { it.copy(credential = it.credential.copy(username = event.username)) }
            is LoginEvent.SetPassword -> _state.update { it.copy(credential = it.credential.copy(password = event.password)) }
            is LoginEvent.ShowPassword -> _state.update{ it.copy(showPassword = event.showPassword) }
            is LoginEvent.Reset -> _state.update { LoginState() }
            is LoginEvent.SignIn -> signIn()
        }
    }

    private fun signIn() = viewModelScope.launch {
        _state.update { it.copy(process = ProcessState.Processing) }
        when (val res = signInUseCase(state.value.credential) ) {
            is Result.Success -> {
                _state.update { it.copy(
                    process = ProcessState.Success(res.data.name),
                    user = res.data
                ) }
            }
            is Result.Failure -> _state.update { it.copy(process = ProcessState.Failure(res.reason)) }
        }
    }
}