package paufregi.connectfeed.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.ConfirmationDialog
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.TextIcon
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Strava

@Composable
@ExperimentalMaterial3Api
internal fun SettingsScreen(nav: NavHostController = rememberNavController()) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(state, viewModel::onAction, viewModel.stravaAuthUri, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun SettingsContent(
    @PreviewParameter(SettingsStatePreview::class) state: SettingsState,
    onAction: (SettingsAction) -> Unit = {},
    stravaAuthUri: Uri = Uri.EMPTY,
    nav: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    var signOutDialog by remember { mutableStateOf(false) }
    var stravaDialog by remember { mutableStateOf(false) }

    if (signOutDialog) {
        ConfirmationDialog(
            title = "Sign out",
            message = "Are you sure you want to sign out?",
            onConfirm = { onAction(SettingsAction.SignOut) },
            onDismiss = { signOutDialog = false },
            modifier = Modifier.testTag("sign_out_dialog")
        )
    }

    if (stravaDialog) {
        ConfirmationDialog(
            title = "Disconnect Strava",
            message = "Are you sure you want to disconnect Strava?",
            onConfirm = { onAction(SettingsAction.StravaDisconnect) },
            onDismiss = { stravaDialog = false },
            modifier = Modifier.testTag("strava_dialog")
        )
    }

    Screen(
        tagName = "settings_screen",
        navigationIndex = Navigation.SETTINGS,
        nav = nav,
        state = state.process,
        success = successInfo { onAction(SettingsAction.Reset) },
        failure = failureInfo { onAction(SettingsAction.Reset) },
    ) {
        AsyncImage(
            model = state.user?.profileImageUrl,
            contentDescription = null,
            modifier = Modifier
                .scale(1.75f)
                .clip(CircleShape)
                .testTag("profileImage")
        )
        Spacer(modifier = Modifier.size(42.dp))
        Text(text = state.user?.name ?: "", fontSize = 24.sp)
        HorizontalDivider()
        Row(verticalAlignment = Alignment.CenterVertically){
            TextIcon(Icons.Connect.Logo, "Garmin", true)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                text = "Refresh",
                onClick = { onAction(SettingsAction.RefreshUser) }
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextIcon(Icons.Strava.Logo, "Strava", state.hasStrava)
            Spacer(modifier = Modifier.weight(1f))
            if (state.hasStrava == true) {
                Button(
                    text = "Remove",
                    onClick = { stravaDialog = true }
                )
            } else {
                Button(
                    text = "Connect",
                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, stravaAuthUri)) }
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            text = "Sign out",
            onClick = { signOutDialog = true }
        )
        HorizontalDivider()
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp)) {
            if (!state.hasUpdate) {
                Text(text = "Version: ${state.currentVersion ?: "Unknown"}")
                Icon(Icons.Default.CheckCircle, "latest version", Modifier.size(32.dp).padding(start = 5.dp), Color.Green)
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Column {
                    Text(text = "Version: ${state.currentVersion ?: "Unknown"}")
                    Text(text = "Update available", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    text = "Update",
                    onClick = { onAction(SettingsAction.Update) },
                    enabled = !state.updating
                )
            }
        }
        if(state.updating) {
            LinearProgressIndicator(modifier = Modifier.padding(top = 10.dp).testTag("updating_bar"))
        }
    }
}
