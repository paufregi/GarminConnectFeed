package paufregi.connectfeed.presentation.syncstrava

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.presentation.HomeNavigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.utils.launchStrava

@Composable
@ExperimentalMaterial3Api
internal fun SyncStravaScreen(nav: NavHostController = rememberNavController()) {
    val viewModel = hiltViewModel<SyncStravaViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SyncStravaContent(state, viewModel::onAction, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun SyncStravaContent(
    @PreviewParameter(SyncStravaStatePreview::class) state: SyncStravaState,
    onAction: (SyncStravaAction) -> Unit = {},
    nav: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    Screen(
        tagName = "sync_strava_screen",
        location = HomeNavigation.SYNC_STRAVA,
        hasStrava = true,
        nav = nav,
        state = state.process,
        success = successInfo {
            onAction(SyncStravaAction.Restart)
            launchStrava(context, state.stravaActivity)
        },
        failure = failureInfo { onAction(SyncStravaAction.Restart) }
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Dropdown(
            label = { Text("Activity") },
            selected = state.activity?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.activities
                .filter { state.stravaActivity?.type == null || it.type == state.stravaActivity.type }
                .map { it.toDropdownItem { onAction(SyncStravaAction.SetActivity(it)) } }
        )
        if (state.stravaActivities.isNotEmpty()) {
            Dropdown(
                label = { Text("Strava activity") },
                selected = state.stravaActivity?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.stravaActivities
                    .filter { state.activity?.type == null || it.type == state.activity.type }
                    .map { it.toDropdownItem { onAction(SyncStravaAction.SetStravaActivity(it)) } }
            )
        }
        TextField(
            label = { Text("Description") },
            value = state.description ?: "",
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            onValueChange = { onAction(SyncStravaAction.SetDescription(it)) }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onAction(SyncStravaAction.SetTrainingEffect(!state.trainingEffect)) })
        ) {
            Checkbox(
                modifier = Modifier.testTag("training_effect_checkbox"),
                checked = state.trainingEffect,
                onCheckedChange = { onAction(SyncStravaAction.SetTrainingEffect(it)) },
            )
            Text(text = "Training effect")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                text = "Reset",
                onClick = { onAction(SyncStravaAction.Restart) }
            )
            Button(
                text = "Save",
                enabled = state.activity != null && state.stravaActivity != null,

                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(SyncStravaAction.Save)
                }
            )
        }
    }
}
