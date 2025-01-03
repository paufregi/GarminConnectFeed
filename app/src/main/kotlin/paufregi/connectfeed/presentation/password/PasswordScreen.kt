package paufregi.connectfeed.presentation.password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.StatusInfo
import paufregi.connectfeed.presentation.ui.components.StatusInfoType
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
internal fun PasswordScreen(nav: NavController = rememberNavController()) {
    val viewModel = hiltViewModel<PasswordViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    PasswordContent(state, viewModel::onEvent, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun PasswordContent(
    @PreviewParameter(PasswordContentStatePreview::class) state: PasswordState,
    onEvent: (PasswordEvent) -> Unit = {},
    nav: NavController = rememberNavController()
) {
    when (state.process) {
        is ProcessState.Processing -> SimpleScaffold { Loading(it) }
        is ProcessState.Success -> SimpleScaffold {
            StatusInfo(
                type = StatusInfoType.Success,
                text = "Password changed",
                actionButton = { Button(text = "Ok", onClick = { nav.navigateUp() }) },
                paddingValues = it
            )
        }
        is ProcessState.Failure -> SimpleScaffold {
            StatusInfo(
                type = StatusInfoType.Failure,
                text = state.process.reason,
                actionButton = { Button(text = "Ok", onClick = { nav.navigateUp() }) },
                paddingValues = it
            )
        }
        is ProcessState.Idle -> NavigationScaffold(
            items = Navigation.items,
            selectIndex = Navigation.ACCOUNT,
            nav = nav
        ) {
            PasswordForm(state, onEvent, nav, it)
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun PasswordForm(
    @PreviewParameter(PasswordFormStatePreview::class) state: PasswordState,
    onEvent: (PasswordEvent) -> Unit = {},
    nav: NavController = rememberNavController(),
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
            .testTag("password_form")
    ) {
        TextField(
            label = { Text("Username") },
            value = state.credential.username,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { },
            enabled = false,
        )
        TextField(
            label = { Text("Password") },
            value = state.credential.password,
            onValueChange =  { onEvent(PasswordEvent.SetPassword(it)) },
            isError = state.credential.password.isBlank(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Button(
                    onClick = { onEvent(PasswordEvent.ShowPassword(!state.showPassword)) },
                    icon = if (state.showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    description = if (state.showPassword) "Hide password" else "Show password"
                )
            }
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                text = "Cancel",
                onClick = { nav.navigateUp() }
            )
            Button(
                text = "Save",
                enabled = state.credential.username.isNotBlank() && state.credential.password.isNotBlank(),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onEvent(PasswordEvent.SignIn)
                }
            )
        }
    }
}

