package paufregi.connectfeed.presentation.app.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.frame.ActivityFrame
import paufregi.connectfeed.presentation.ui.components.frame.BottomApp
import paufregi.connectfeed.presentation.ui.components.loading.Loading
import paufregi.connectfeed.presentation.ui.components.status.ActivityFailure
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
internal fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginContent(state, viewModel::onAction)
}

@Composable
@ExperimentalMaterial3Api
internal fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit = {},
) {
    when(val p = state.status) {
        is Status.Loading -> Loading()
        is Status.Success -> Welcome(state, onAction)
        is Status.Failure -> ActivityFailure("Connect Feed", null, "Login failed!", p.reason) { onAction(LoginAction.Reset) }
        else -> LoginForm(state, onAction)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun LoginForm(
    @PreviewParameter(LoginStatePreview::class) state: LoginState,
    onAction: (LoginAction) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    ActivityFrame(title = "Login", testTag = "login_form") {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Garmin Connect",
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 20.dp).testTag("username")
            )
            Text(
                text = "Sign in with your Garmin Connect credentials",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 5.dp)
            )
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

@Composable
@ExperimentalMaterial3Api
fun Welcome(
    state: LoginState,
    onAction: (LoginAction) -> Unit = {},
) {
    ActivityFrame(
        title = "Connect Feed",
        testTag = "welcome_screen",
        bottomContent = BottomApp.ok { onAction(LoginAction.SaveUser) }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(20.dp))
        ) {
            AsyncImage(
                model = state.user?.profileImageUrl,
                contentDescription = null,
                modifier = Modifier.scale(1.75f).clip(CircleShape)
            )
            Spacer(modifier = Modifier.size(42.dp))
            Text(
                text = "Welcome ${state.user?.name ?: "Unknown"}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 50.dp)
            )
        }
    }
}
