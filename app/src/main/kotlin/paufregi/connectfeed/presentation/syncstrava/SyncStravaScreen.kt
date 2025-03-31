package paufregi.connectfeed.presentation.syncstrava

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.presentation.HomeNavigation
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.NavigationBar
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.StatusInfo
import paufregi.connectfeed.presentation.ui.components.StatusInfoType
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
internal fun SyncStravaScreen(nav: NavController = rememberNavController()) {
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
    nav: NavController = rememberNavController()
) {
    when (state.process) {
        is ProcessState.Processing -> SimpleScaffold { Loading(it) }
        is ProcessState.Success -> SimpleScaffold {
            StatusInfo(
                type = StatusInfoType.Success,
                text = state.process.message ?: "All done",
                actionButton = {
                    Button(
                        text = "Ok",
                        onClick = { onAction(SyncStravaAction.Restart) })
                },
                paddingValues = it
            )
        }

        is ProcessState.Failure -> SimpleScaffold {
            StatusInfo(
                type = StatusInfoType.Failure,
                text = state.process.reason,
                actionButton = {
                    Button(
                        text = "Ok",
                        onClick = { onAction(SyncStravaAction.Restart) })
                },
                paddingValues = it
            )
        }

        is ProcessState.Idle -> NavigationScaffold(
            items = Navigation.items,
            selectedIndex = Navigation.HOME,
            bottomBar = {
                NavigationBar(
                    items = HomeNavigation.items(true),
                    selectedIndex = HomeNavigation.SYNC_STRAVA,
                    nav = nav
                )
            },
            nav = nav
        ) { SyncStravaForm(state, onAction, it) }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun SyncStravaForm(
    @PreviewParameter(SyncStravaStatePreview::class) state: SyncStravaState,
    onAction: (SyncStravaAction) -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr) + 20.dp,
                end = paddingValues.calculateRightPadding(LayoutDirection.Ltr) + 20.dp,
            )
            .testTag("sync_strava_form")
    ) {
        remember { MutableInteractionSource() }

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
                label = { Text("Strava Activity") },
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
            Text("Training Effect")
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
