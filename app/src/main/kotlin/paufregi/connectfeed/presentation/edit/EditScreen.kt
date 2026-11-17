package paufregi.connectfeed.presentation.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.HomeNavigation
import paufregi.connectfeed.presentation.Navigator
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.EffortSlider
import paufregi.connectfeed.presentation.ui.components.FeelSelector
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successActivityUpdate
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.utils.launchGarmin
import paufregi.connectfeed.presentation.ui.utils.launchStrava

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun EditScreen(nav: Navigator = Navigator()) {
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
    nav: Navigator = Navigator()
) {
    val context = LocalContext.current

    Screen(
        tagName = "edit_screen",
        location = HomeNavigation.EDIT,
        nav = nav,
        state = state.process,
        success = successActivityUpdate(
            action = { onAction(EditAction.Restart) },
            garmin = { launchGarmin(context, state.activity) },
            strava = { launchStrava(context, state.stravaActivity) }
        ),
        failure = failureInfo { onAction(EditAction.Restart) },
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }

        Dropdown(
            label = { Text("Activity") },
            selected = state.activity?.toDropdownItem(stravaActivity = state.stravaActivity),
            modifier = Modifier.fillMaxWidth(),
            items = state.activities.map {
                it.toDropdownItem(onClick = {
                    onAction(EditAction.SetActivity(it))
                })
            }
        )

        if (state.activity != null) {
            TextField(
                label = { Text("Name") },
                value = state.name ?: "",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                onValueChange = { onAction(EditAction.SetName(it)) }
            )
            Dropdown(
                label = { Text("Event type") },
                selected = state.eventType?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.eventTypes
                    .map { it.toDropdownItem { onAction(EditAction.SetEventType(it)) } },
            )
            if (state.activity.type.allowCourse) {
                Dropdown(
                    label = { Text("Course") },
                    selected = state.course?.toDropdownItem { },
                    modifier = Modifier.fillMaxWidth(),
                    items = state.courses
                        .filter { it.type.compatible(state.activity.type) }
                        .map { it.toDropdownItem { onAction(EditAction.SetCourse(it)) } }
                )
            }
            if (state.gears.any { it.type.compatible(state.activity.type) }) {
                Dropdown(
                    label = { Text("Gear") },
                    selected = state.gear?.toDropdownItem { },
                    modifier = Modifier.fillMaxWidth(),
                    items = state.gears
                        .filter { it.type.compatible(state.activity.type) }
                        .map { it.toDropdownItem { onAction(EditAction.SetGear(it)) } }
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
            FeelSelector(
                value = state.feel,
                onChange = { onAction(EditAction.SetFeel(it)) }
            )
            EffortSlider(
                value = state.effort,
                onChange = { onAction(EditAction.SetEffort(it)) }
            )
            if (state.stravaActivity != null) {
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
                enabled = state.activity != null,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(EditAction.Save)
                }
            )
        }
    }
}
