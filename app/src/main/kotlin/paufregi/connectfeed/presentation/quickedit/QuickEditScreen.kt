package paufregi.connectfeed.presentation.quickedit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
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
import androidx.compose.ui.text.input.KeyboardType
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
import paufregi.connectfeed.presentation.ui.components.CustomSlider
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.IconRadioGroup
import paufregi.connectfeed.presentation.ui.components.IconRadioItem
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.NavigationBar
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.StatusInfo
import paufregi.connectfeed.presentation.ui.components.StatusInfoType
import paufregi.connectfeed.presentation.ui.components.TextEffort
import paufregi.connectfeed.presentation.ui.components.TextFeel
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.icons.Connect
import paufregi.connectfeed.presentation.ui.icons.FaceHappy
import paufregi.connectfeed.presentation.ui.icons.FaceNormal
import paufregi.connectfeed.presentation.ui.icons.FaceSad
import paufregi.connectfeed.presentation.ui.icons.FaceVeryHappy
import paufregi.connectfeed.presentation.ui.icons.FaceVerySad
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
internal fun QuickEditScreen(nav: NavController = rememberNavController()) {
    val viewModel = hiltViewModel<QuickEditViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    QuickEditContent(state, viewModel::onAction, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun QuickEditContent(
    @PreviewParameter(QuickEditStatePreview::class) state: QuickEditState,
    onAction: (QuickEditAction) -> Unit = {},
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
                        onClick = { onAction(QuickEditAction.Restart) })
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
                        onClick = { onAction(QuickEditAction.Restart) })
                },
                paddingValues = it
            )
        }

        is ProcessState.Idle -> NavigationScaffold(
            items = Navigation.items,
            selectedIndex = Navigation.HOME,
            bottomBar = {
                NavigationBar(
                    items = HomeNavigation.items(state.hasStrava),
                    selectedIndex = HomeNavigation.QUICK_EDIT,
                    nav = nav
                )
            },
            nav = nav
        ) { QuickEditForm(state, onAction, it) }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun QuickEditForm(
    @PreviewParameter(QuickEditStatePreview::class) state: QuickEditState,
    onAction: (QuickEditAction) -> Unit = {},
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
            .testTag("quick_edit_form")
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        Dropdown(
            label = { Text("Activity") },
            selected = state.activity?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.activities
                .filter { state.stravaActivity?.type == null || it.type == state.stravaActivity.type }
                .map {
                    it.toDropdownItem {
                        onAction(QuickEditAction.SetActivity(it))
                    }
                }
        )
        if (state.hasStrava) {
            Dropdown(
                label = { Text("Strava Activity") },
                selected = state.stravaActivity?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.stravaActivities
                    .filter { state.activity?.type == null || it.type == state.activity.type }
                    .map {
                        it.toDropdownItem {
                            onAction(QuickEditAction.SetStravaActivity(it))
                        }
                    }
            )
        }
        Dropdown(
            label = { Text("Profile") },
            selected = state.profile?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.profiles
                .filter {
                    (state.activity?.type == null || it.activityType == state.activity.type) &&
                            (state.stravaActivity?.type == null || it.activityType == state.stravaActivity.type)
                }
                .map {
                    it.toDropdownItem { onAction(QuickEditAction.SetProfile(it)) }
                }
        )
        if (state.stravaActivity != null) {
            TextField(
                label = { Text("Description") },
                value = state.description ?: "",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                onValueChange = { onAction(QuickEditAction.SetDescription(it)) }
            )
        }
        if (state.profile?.customWater == true) {
            TextField(
                label = { Text("Water") },
                value = state.profile.water?.toString() ?: "",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                onValueChange = { onAction(QuickEditAction.SetWater(it.toIntOrNull())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        if (state.profile?.feelAndEffort == true) {
            Column {
                IconRadioGroup(
                    options = listOf(
                        IconRadioItem(0f, Icons.Connect.FaceVerySad),
                        IconRadioItem(25f, Icons.Connect.FaceSad),
                        IconRadioItem(50f, Icons.Connect.FaceNormal),
                        IconRadioItem(75f, Icons.Connect.FaceHappy),
                        IconRadioItem(100f, Icons.Connect.FaceVeryHappy),
                    ),
                    selected = state.feel,
                    onClick = { onAction(QuickEditAction.SetFeel(it)) }
                )
                TextFeel(
                    state.feel,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 10.dp)
                )
            }
            Column {
                Slider(
                    value = state.effort ?: 0f,
                    onValueChange = { onAction(QuickEditAction.SetEffort(it.toInt().toFloat())) },
                    valueRange = 0f..100f,
                    steps = 9,
                    interactionSource = interactionSource,
                    track = CustomSlider.track,
                    thumb = CustomSlider.thumb(interactionSource),
                    modifier = Modifier.fillMaxWidth()
                )
                TextEffort(
                    state.effort ?: 0f,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Button(
                text = "Reset",
                onClick = { onAction(QuickEditAction.Restart) }
            )

            Button(
                text = "Save",
                enabled = state.activity != null && state.profile != null &&
                        (!state.hasStrava || state.stravaActivity != null),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(QuickEditAction.Save)
                }
            )
        }
    }
}
