package paufregi.connectfeed.presentation.edit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.HomeNavigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.EffortSlider
import paufregi.connectfeed.presentation.ui.components.FeelRadioGroup
import paufregi.connectfeed.presentation.ui.components.RowCheckBox
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.utils.launchStrava

@Composable
@ExperimentalMaterial3Api
internal fun EditScreen(nav: NavHostController = rememberNavController()) {
    val viewModel = hiltViewModel<EditViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditContent(state, viewModel::onAction, nav)
}

@Composable
@ExperimentalMaterial3Api
internal fun EditContent(
    state: EditState,
    onAction: (EditAction) -> Unit = {},
    nav: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    Screen(
        tagName = "edit_form",
        location = HomeNavigation.EDIT,
        hasStrava = state.hasStrava,
        nav = nav,
        state = state.process,
        success = successInfo {
            onAction(EditAction.Restart)
            launchStrava(context, state.stravaActivity)
        },
        failure = failureInfo { onAction(EditAction.Restart) },
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }

        Dropdown(
            label = { Text("Activity") },
            selected = state.activity?.toDropdownItem { },
            items = state.activities
                .filter { state.stravaActivity?.type == null || it.type == state.stravaActivity.type }
                .map { it.toDropdownItem { onAction(EditAction.SetActivity(it)) } }
        )
        if (state.hasStrava) {
            Dropdown(
                label = { Text("Strava activity") },
                selected = state.stravaActivity?.toDropdownItem(),
                items = state.stravaActivities
                    .filter { state.activity?.type == null || it.type == state.activity.type }
                    .map { it.toDropdownItem { onAction(EditAction.SetStravaActivity(it)) } }
            )
        }
        TextField(
            label = { Text("Name") },
            value = state.name ?: "",
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            onValueChange = { onAction(EditAction.SetName(it)) }
        )
        Dropdown(
            label = { Text("Event type") },
            selected = state.eventType?.toDropdownItem(),
            items = state.eventTypes
                .map { it.toDropdownItem { onAction(EditAction.SetEventType(it)) } },
        )
        if (state.activity?.type?.allowCourseInProfile == true) {
            Dropdown(
                label = { Text("Course") },
                selected = state.course?.toDropdownItem(),
                items = state.courses
                    .filter { it.type == state.activity.type || state.activity.type == ActivityType.Any }
                    .map { it.toDropdownItem { onAction(EditAction.SetCourse(it)) } }
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
        FeelRadioGroup(
            value = state.feel ?: 0f,
            onValueChange = { onAction(EditAction.SetFeel(it)) }
        )
        EffortSlider(
            value = state.effort,
            onValueChange = { onAction(EditAction.SetEffort(it)) },
            interactionSource = interactionSource
        )
        if (state.stravaActivity != null) {
            RowCheckBox(
                checked = state.trainingEffect,
                onCheckedChange = { onAction(EditAction.SetTrainingEffect(it)) },
                label = "Training effect",
            )
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
