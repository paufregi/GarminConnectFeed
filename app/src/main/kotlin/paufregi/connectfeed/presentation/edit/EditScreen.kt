package paufregi.connectfeed.presentation.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.HomeNavigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.CustomSlider
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.IconRadioGroup
import paufregi.connectfeed.presentation.ui.components.IconRadioItem
import paufregi.connectfeed.presentation.ui.components.Screen
import paufregi.connectfeed.presentation.ui.components.TextEffort
import paufregi.connectfeed.presentation.ui.components.TextFeel
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.icons.Connect
import paufregi.connectfeed.presentation.ui.icons.FaceHappy
import paufregi.connectfeed.presentation.ui.icons.FaceNormal
import paufregi.connectfeed.presentation.ui.icons.FaceSad
import paufregi.connectfeed.presentation.ui.icons.FaceVeryHappy
import paufregi.connectfeed.presentation.ui.icons.FaceVerySad
import paufregi.connectfeed.presentation.ui.utils.launchStrava

@Composable
@ExperimentalMaterial3Api
internal fun EditScreen(nav: NavHostController = rememberNavController()) {
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
    nav: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    Screen(
        tagName = "edit_screen",
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
            modifier = Modifier.fillMaxWidth(),
            items = state.activities
                .filter { state.stravaActivity?.type == null || it.type == state.stravaActivity.type }
                .map { it.toDropdownItem { onAction(EditAction.SetActivity(it)) } }
        )
        if (state.hasStrava) {
            Dropdown(
                label = { Text("Strava activity") },
                selected = state.stravaActivity?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
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
            selected = state.eventType?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.eventTypes
                .map { it.toDropdownItem { onAction(EditAction.SetEventType(it)) } },
        )
        if (state.activity?.type?.allowCourseInProfile == true) {
            Dropdown(
                label = { Text("Course") },
                selected = state.course?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
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
                    .testTag("feel_text")
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
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag("effort_text")
            )
        }
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
