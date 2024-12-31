package paufregi.connectfeed.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.StatusInfo
import paufregi.connectfeed.presentation.ui.components.StatusInfoType
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
internal fun LoginScreen(
    onLogin: () -> Unit = {}
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SimpleScaffold { LoginContent(state, viewModel::onEvent, onLogin, it) }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun LoginContent(
    @PreviewParameter(LoginContentStatePreview::class) state: LoginState,
    onEvent: (LoginEvent) -> Unit = {},
    onLogin: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(),
) {
    when (state.process) {
        is ProcessState.Processing -> Loading(paddingValues)
        is ProcessState.Success -> WelcomeInfo(
            name = state.user?.name ?: "",
            url = state.user?.profileImageUrl ?: "",
            actionButton = { Button(text = "Ok", onClick = onLogin) },
            paddingValues = paddingValues
        )
        is ProcessState.Failure -> StatusInfo(
            type = StatusInfoType.Failure,
            text = state.process.reason,
            actionButton = { Button(text = "Ok", onClick = { onEvent(LoginEvent.Reset) }) },
            paddingValues = paddingValues
        )
        is ProcessState.Idle -> LoginForm(state, onEvent, paddingValues)
    }
}

@Composable
@ExperimentalMaterial3Api
fun WelcomeInfo(
    name: String,
    url: String,
    actionButton: @Composable () -> Unit = { },
    paddingValues: PaddingValues = PaddingValues()
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.scale(2.5f).clip(CircleShape)
        )
        Spacer(modifier = Modifier.size(38.dp))
        Text(text = "Welcome $name", fontSize = 24.sp)// color = MaterialTheme.colorScheme.onPrimaryContainer)
        Spacer(modifier = Modifier.size(24.dp))
        actionButton()
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun LoginForm(
    @PreviewParameter(LoginFormStatePreview::class) state: LoginState,
    onEvent: (LoginEvent) -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
    ) {
        TextField(
            label = { Text("Username") },
            value = state.credential.username,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onEvent(LoginEvent.SetUsername(it)) },
            isError = state.credential.username.isBlank(),
        )
        TextField(
            label = { Text("Password") },
            value = state.credential.password,
            onValueChange =  { onEvent(LoginEvent.SetPassword(it)) },
            isError = state.credential.password.isBlank(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Button(
                    onClick = { onEvent(LoginEvent.ShowPassword(!state.showPassword)) },
                    icon = if (state.showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    description = if (state.showPassword) "Hide password" else "Show password"
                )
            }
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                text = "Sign in",
                enabled = state.credential.username.isNotBlank() && state.credential.password.isNotBlank(),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onEvent(LoginEvent.SignIn)
                }
            )
        }
    }
}

