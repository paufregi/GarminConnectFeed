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
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
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


    ProfileContent(state, viewModel::onEvent, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ProfileContent(
    @PreviewParameter(ProfileStatePreview::class) state: ProfileState,
    onEvent: (ProfileEvent) -> Unit = {},
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
        ) { ProfileForm(state, onEvent, nav, it) }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ProfileForm(
    @PreviewParameter(ProfileStatePreview::class) state: ProfileState,
    onEvent: (ProfileEvent) -> Unit = {},
    nav: NavHostController = rememberNavController(),
    paddingValues: PaddingValues = PaddingValues()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("profile_form")
    ) {
        TextField(
            label = { Text("Name") },
            value = state.profile.name,
            onValueChange = { onEvent(ProfileEvent.SetName(it)) },
            isError = state.profile.name.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        Dropdown(
            label = { Text("Activity Type") },
            selected = state.profile.activityType.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.activityTypes.map {
                it.toDropdownItem { onEvent(ProfileEvent.SetActivityType(it)) }
            }
        )
        Dropdown(
            label = { Text("Event Type") },
            selected = state.profile.eventType?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.eventTypes.map {
                it.toDropdownItem {
                    onEvent(ProfileEvent.SetEventType(it))
                }
            },
            isError = state.profile.activityType != ActivityType.Any && state.profile.eventType == null

        )
        if (state.profile.activityType != ActivityType.Any && state.profile.activityType != ActivityType.Strength) {
            Dropdown(
                label = { Text("Course") },
                selected = state.profile.course?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.courses
                    .filter { it.type == state.profile.activityType || state.profile.activityType == ActivityType.Any }
                    .map {
                        it.toDropdownItem { onEvent(ProfileEvent.SetCourse(it)) }
                    }
            )
        }
        TextField(
            label = { Text("Water") },
            value = state.profile.water?.toString() ?: "",
            onValueChange = { onEvent(ProfileEvent.SetWater(it.toIntOrNull()))   },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    onEvent(ProfileEvent.SetRename(!state.profile.rename))
                })
        ) {
            Checkbox(
                modifier = Modifier.testTag("rename_checkbox"),
                checked = state.profile.rename,
                onCheckedChange = { onEvent(ProfileEvent.SetRename(it)) },
            )
            Text("Rename activity")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().clickable(
                onClick = { onEvent(ProfileEvent.SetCustomWater(!state.profile.customWater)) }
            )
        ) {
            Checkbox(
                modifier = Modifier.testTag("custom_water_checkbox"),
                checked = state.profile.customWater,
                onCheckedChange = { onEvent(ProfileEvent.SetCustomWater(it)) },
            )
            Text("Customizable water")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().clickable(
                onClick = { onEvent(ProfileEvent.SetFeelAndEffort(!state.profile.feelAndEffort)) }
            )
        ) {
            Checkbox(
                modifier = Modifier.testTag("feel_and_effort_checkbox"),
                checked = state.profile.feelAndEffort,
                onCheckedChange = { onEvent(ProfileEvent.SetFeelAndEffort(it)) },
            )
            Text(text = "Feel & Effort")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
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
                    onEvent(ProfileEvent.Save) }
            )
        }
    }
}
