package paufregi.connectfeed.presentation.edit

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Checkbox
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
import paufregi.connectfeed.core.models.ActivityType
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
internal fun EditScreen(nav: NavController = rememberNavController()) {
    val viewModel = hiltViewModel<EditViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditContent(state, viewModel::onAction, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun EditContent(
    @PreviewParameter(EditStatePreview::class) state: EditState,
    onAction: (EditAction) -> Unit = {},
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
                        onClick = { onAction(EditAction.Restart) })
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
                        onClick = { onAction(EditAction.Restart) })
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
                    selectedIndex = HomeNavigation.EDIT,
                    nav = nav
                )
            },
            nav = nav
        ) { EditForm(state, onAction, it) }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun EditForm(
    @PreviewParameter(EditStatePreview::class) state: EditState,
    onAction: (EditAction) -> Unit = {},
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
            .testTag("edit_form")
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
                        onAction(EditAction.SetActivity(it))
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
                            onAction(EditAction.SetStravaActivity(it))
                        }
                    }
            )
        }
        Dropdown(
            label = { Text("Event Type") },
            selected = state.eventType?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.eventTypes.map {
                it.toDropdownItem {
                    onAction(EditAction.SetEventType(it))
                }
            },
        )
        if (state.activity?.type?.allowCourseInProfile == true) {
            Dropdown(
                label = { Text("Course") },
                selected = state.course?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.courses
                    .filter { it.type == state.activity.type || state.activity.type == ActivityType.Any }
                    .map {
                        it.toDropdownItem { onAction(EditAction.SetCourse(it)) }
                    }
            )
        }
        if (state.stravaActivity != null) {
            TextField(
                label = { Text("Description") },
                value = state.description ?: "",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                onValueChange = { onAction(EditAction.SetDescription(it)) }
            )
        }
        TextField(
            label = { Text("Water") },
            value = state.water?.toString() ?: "",
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            onValueChange = { onAction(EditAction.SetWater(it.toIntOrNull())) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
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
                onClick = { onAction(EditAction.SetFeel(it)) }
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
                onValueChange = { onAction(EditAction.SetEffort(it.toInt().toFloat())) },
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
        if (state.hasStrava) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { onAction(EditAction.SetTrainingEffect(!state.trainingEffect)) }
                    )
            ) {
                Checkbox(
                    modifier = Modifier.testTag("training_effect_checkbox"),
                    checked = state.trainingEffect,
                    onCheckedChange = { onAction(EditAction.SetTrainingEffect(it)) },
                )
                Text(text = "Training effect")
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Button(
                text = "Reset",
                onClick = { onAction(EditAction.Restart) }
            )

            Button(
                text = "Save",
                enabled = state.activity != null &&
                        (!state.hasStrava || state.stravaActivity != null),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(EditAction.Save)
                }
            )
        }
    }
}
