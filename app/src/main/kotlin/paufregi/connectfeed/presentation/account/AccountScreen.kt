package paufregi.connectfeed.presentation.account

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.ConfirmationDialog
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo

@Composable
@ExperimentalMaterial3Api
internal fun AccountScreen(nav: NavHostController = rememberNavController()) {
    val viewModel = hiltViewModel<AccountViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AccountContent(state, viewModel::onAction, viewModel.stravaAuthUri, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun AccountContent(
    @PreviewParameter(AccountStatePreview::class) state: AccountState,
    onAction: (AccountAction) -> Unit = {},
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
            onConfirm = { onAction(AccountAction.SignOut) },
            onDismiss = { signOutDialog = false },
            modifier = Modifier.testTag("sign_out_dialog")
        )
    }

    if (stravaDialog) {
        ConfirmationDialog(
            title = "Disconnect Strava",
            message = "Are you sure you want to disconnect Strava?",
            onConfirm = { onAction(AccountAction.StravaDisconnect) },
            onDismiss = { stravaDialog = false },
            modifier = Modifier.testTag("strava_dialog")
        )
    }

    Screen(
        tagName = "account_form",
        navigationIndex = Navigation.ACCOUNT,
        nav = nav,
        state = state.process,
        success = successInfo { onAction(AccountAction.Reset) },
        failure = failureInfo { onAction(AccountAction.Reset) },
    ) {
        AsyncImage(
            model = state.user?.profileImageUrl,
            contentDescription = null,
            modifier = Modifier
                .scale(2f)
                .clip(CircleShape)
                .testTag("profileImage")
        )
        Spacer(modifier = Modifier.size(42.dp))
        Text(text = state.user?.name ?: "", fontSize = 24.sp)
        Spacer(modifier = Modifier.size(24.dp))
        Button(
            text = "Refresh user",
            onClick = { onAction(AccountAction.RefreshUser) }
        )
        if (state.hasStrava == true) {
            Button(
                text = "Disconnect Strava",
                onClick = { stravaDialog = true }
            )
        } else {
            Button(
                text = "Connect Strava",
                onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, stravaAuthUri)) }
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            text = "Sign out",
            onClick = { signOutDialog = true }
        )
    }
}
