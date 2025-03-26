package paufregi.connectfeed.presentation.profile

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.StatusInfo
import paufregi.connectfeed.presentation.ui.components.StatusInfoType
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
internal fun ProfileScreen(
    nav: NavHostController = rememberNavController()
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileContent(state, viewModel::onAction, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ProfileContent(
    @PreviewParameter(ProfileStatePreview::class) state: ProfileState,
    onAction: (ProfileAction) -> Unit = {},
    nav: NavHostController = rememberNavController()
) {
    when (state.process) {
        is ProcessState.Processing -> SimpleScaffold { Loading(it) }
        is ProcessState.Success -> SimpleScaffold {
            StatusInfo(
                type = StatusInfoType.Success,
                text = state.process.message ?: "All done",
                actionButton = { Button(text = "Ok", onClick = { nav.navigateUp() }) },
                paddingValues = it
            )
        }

        is ProcessState.Failure -> SimpleScaffold {
            StatusInfo(
                type = StatusInfoType.Failure,
                text = state.process.reason,
                actionButton = { Button(text = "Ok", onClick = { nav.navigateUp() }) },
                paddingValues = it
            )
        }

        is ProcessState.Idle -> NavigationScaffold(
            items = Navigation.items,
            selectedIndex = Navigation.PROFILES,
            nav = nav
        ) { ProfileForm(state, onAction, nav, it) }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ProfileForm(
    @PreviewParameter(ProfileStatePreview::class) state: ProfileState,
    onAction: (ProfileAction) -> Unit = {},
    nav: NavHostController = rememberNavController(),
    paddingValues: PaddingValues = PaddingValues()
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
            .testTag("profile_form")
    ) {
        TextField(
            label = { Text("Name") },
            value = state.profile.name,
            onValueChange = { onAction(ProfileAction.SetName(it)) },
            isError = state.profile.name.isBlank(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Dropdown(
            label = { Text("Activity Type") },
            selected = state.profile.activityType.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.activityTypes.map {
                it.toDropdownItem { onAction(ProfileAction.SetActivityType(it)) }
            }
        )
        Dropdown(
            label = { Text("Event Type") },
            selected = state.profile.eventType?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.eventTypes.map {
                it.toDropdownItem {
                    onAction(ProfileAction.SetEventType(it))
                }
            },
            isError = state.profile.activityType != ActivityType.Any && state.profile.eventType == null

        )
        if (state.profile.activityType.allowCourseInProfile == true) {
            Dropdown(
                label = { Text("Course") },
                selected = state.profile.course?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.courses
                    .filter { it.type == state.profile.activityType || state.profile.activityType == ActivityType.Any }
                    .map {
                        it.toDropdownItem { onAction(ProfileAction.SetCourse(it)) }
                    }
            )
        }
        TextField(
            label = { Text("Water") },
            value = state.profile.water?.toString() ?: "",
            onValueChange = { onAction(ProfileAction.SetWater(it.toIntOrNull())) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    onAction(ProfileAction.SetRename(!state.profile.rename))
                })
        ) {
            Checkbox(
                modifier = Modifier.testTag("rename_checkbox"),
                checked = state.profile.rename,
                onCheckedChange = { onAction(ProfileAction.SetRename(it)) },
            )
            Text("Rename activity")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { onAction(ProfileAction.SetCustomWater(!state.profile.customWater)) }
                )
        ) {
            Checkbox(
                modifier = Modifier.testTag("custom_water_checkbox"),
                checked = state.profile.customWater,
                onCheckedChange = { onAction(ProfileAction.SetCustomWater(it)) },
            )
            Text("Customizable water")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { onAction(ProfileAction.SetFeelAndEffort(!state.profile.feelAndEffort)) }
                )
        ) {
            Checkbox(
                modifier = Modifier.testTag("feel_and_effort_checkbox"),
                checked = state.profile.feelAndEffort,
                onCheckedChange = { onAction(ProfileAction.SetFeelAndEffort(it)) },
            )
            Text(text = "Feel & Effort")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { onAction(ProfileAction.SetTrainingEffect(!state.profile.trainingEffect)) }
                )
        ) {
            Checkbox(
                modifier = Modifier.testTag("training_effect_checkbox"),
                checked = state.profile.trainingEffect,
                onCheckedChange = { onAction(ProfileAction.SetTrainingEffect(it)) },
            )
            Text(text = "Training effect")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Button(
                text = "Cancel",
                onClick = { nav.navigateUp() }
            )

            Button(
                text = "Save",
                enabled = state.profile.name.isNotBlank() &&
                        (state.profile.activityType == ActivityType.Any || state.profile.eventType != null),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(ProfileAction.Save)
                }
            )
        }
    }
}
