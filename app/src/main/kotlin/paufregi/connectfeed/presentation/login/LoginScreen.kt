package paufregi.connectfeed.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.WelcomeInfo
import paufregi.connectfeed.presentation.ui.components.failureInfo

@Composable
@ExperimentalMaterial3Api
internal fun LoginScreen(
    onLogin: () -> Unit = {}
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginContent(state, viewModel::onAction, onLogin)
}

@Composable
@ExperimentalMaterial3Api
internal fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit = {},
    onLogin: () -> Unit = {},
) {
    Screen(
        state = state.process,
        success = {_, pv -> WelcomeInfo(state.user, onLogin, pv) },
        failure = failureInfo { onAction(LoginAction.Reset) }
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp)
                .testTag("login_form")
        ) {
            TextField(
                label = { Text("Username") },
                value = state.username,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onAction(LoginAction.SetUsername(it)) },
                isError = state.username.isBlank(),
            )
            TextField(
                label = { Text("Password") },
                value = state.password,
                onValueChange = { onAction(LoginAction.SetPassword(it)) },
                isError = state.password.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Button(
                        onClick = { onAction(LoginAction.ShowPassword(!state.showPassword)) },
                        icon = if (state.showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        description = if (state.showPassword) "Hide password" else "Show password"
                    )
                }
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Button(
                    text = "Sign in",
                    enabled = state.username.isNotBlank() && state.password.isNotBlank(),
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onAction(LoginAction.SignIn)
                    }
                )
            }
        }
    }
}

