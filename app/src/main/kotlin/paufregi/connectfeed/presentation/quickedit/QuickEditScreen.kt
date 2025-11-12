package paufregi.connectfeed.presentation.quickedit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
import paufregi.connectfeed.presentation.ui.components.successActivityUpdate
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceHappy
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceNormal
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceSad
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceVeryHappy
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceVerySad
import paufregi.connectfeed.presentation.ui.utils.launchGarmin
import paufregi.connectfeed.presentation.ui.utils.launchStrava

@Composable
@ExperimentalMaterial3Api
internal fun QuickEditScreen(nav: NavHostController = rememberNavController()) {
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
    nav: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    Screen(
        tagName = "quick_edit_screen",
        location = HomeNavigation.QUICK_EDIT,
        hasStrava = state.hasStrava,
        nav = nav,
        state = state.process,
        success = successActivityUpdate(
            action = { onAction(QuickEditAction.Restart) },
            garmin = { launchGarmin(context, state.activity) },
            strava = { launchStrava(context, state.stravaActivity) }
        ),
        failure = failureInfo {
            onAction(QuickEditAction.Restart)
        },
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }

        Dropdown(
            label = { Text("Activity") },
            selected = state.activity?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.activities
                .filter { state.stravaActivity == null || it.type.compatible(state.stravaActivity.type) }
                .map {
                    it.toDropdownItem {
                        onAction(QuickEditAction.SetActivity(it))
                    }
                }
        )
        if (state.hasStrava) {
            Dropdown(
                label = { Text("Strava activity") },
                selected = state.stravaActivity?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.stravaActivities
                    .filter { state.activity == null || it.type.compatible(state.activity.type) }
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
                    (state.activity == null || it.type.compatible(state.activity.type)) &&
                        (state.stravaActivity == null || it.type.compatible(state.stravaActivity.type))
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
                value = state.water?.toString() ?: "",
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
