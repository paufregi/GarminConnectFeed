package paufregi.connectfeed.presentation.app.profiles.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.DropdownItem
import paufregi.connectfeed.presentation.ui.components.Toggle
import paufregi.connectfeed.presentation.ui.components.screens.BackScreen
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun ProfileScreen(
    id: Long?,
    onDone: () -> Unit,
) {
    val viewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory>(
        creationCallback = { it.create(id) })

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect is ProfileEffect.NavigateBack) onDone()
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    BackScreen(
        title = id?.let { "Edit profile" } ?: "Create profile",
        isLoading = state.loading,
        navigateBack = onDone,
        bottomBarContent = {
            Button(
                text = "Save profile",
                modifier = Modifier.fillMaxWidth().testTag("save"),
                enabled = state.canSave,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    viewModel.onAction(ProfileAction.Save)
                }
            )
        }
    ) { padding ->
        ProfileForm(
                state = state,
                onAction = viewModel::onAction,
                padding = padding
        )
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ProfileForm(
    @PreviewParameter(ProfilePreview::class) state: ProfileState,
    onAction: (ProfileAction) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .consumeWindowInsets(padding)
            .padding(padding.add(horizontal = 20.dp))
            .testTag("profile_form_content")
    ) {
        TextField(
            label = { Text("Name") },
            value = state.profile.name,
            onValueChange = { onAction(ProfileAction.SetName(it)) },
            isError = state.profile.name.isBlank(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("profile_name")
        )
        Dropdown(
            label = { Text("Type") },
            selected = state.profile.type.toDropdownItem { },
            modifier = Modifier.fillMaxWidth().testTag("profile_type"),
            items = state.activityTypes.map { type ->
                type.toDropdownItem { onAction(ProfileAction.SetType(type)) }
            }
        )
        Dropdown(
            label = { Text("Event type") },
            selected = state.profile.eventType?.toDropdownItem { } ?: DropdownItem("None"),
            modifier = Modifier.fillMaxWidth().testTag("profile_event_type"),
            items = buildList {
                add(DropdownItem("None") { onAction(ProfileAction.SetEventType(null)) })
                addAll(state.eventTypes.map { type ->
                    type.toDropdownItem { onAction(ProfileAction.SetEventType(type)) }
                })
            },
            isError = state.profile.type != ActivityType.Any && state.profile.eventType == null
        )
        if (state.profile.type.allowCourse) {
            Dropdown(
                label = { Text("Course") },
                selected = state.profile.course?.toDropdownItem { } ?: DropdownItem("None"),
                modifier = Modifier.fillMaxWidth().testTag("profile_course"),
                items = buildList {
                    add(DropdownItem("None") { onAction(ProfileAction.SetCourse(null)) })
                    addAll(state.availableCourses.map { course ->
                        course.toDropdownItem { onAction(ProfileAction.SetCourse(course)) }
                    })
                }
            )
        }
        TextField(
            label = { Text("Water") },
            value = state.profile.water?.toString() ?: "",
            onValueChange = { onAction(ProfileAction.SetWater(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().testTag("profile_water")
        )
        Toggle(
            label = "Rename activity",
            checked = state.profile.rename,
            onChange = { onAction(ProfileAction.SetRename(it)) },
            modifier = Modifier.testTag("rename_toggle"),
        )
        Toggle(
            label = "Customizable water",
            checked = state.profile.customWater,
            onChange = { onAction(ProfileAction.SetCustomWater(it)) },
            modifier = Modifier.testTag("custom_water_toggle"),
        )
        Toggle(
            label = "Set gear",
            checked = state.profile.gear,
            onChange = { onAction(ProfileAction.SetGear(it)) },
            modifier = Modifier.testTag("gear_toggle"),
        )
        Toggle(
            label = "Feel & Effort",
            checked = state.profile.feelAndEffort,
            onChange = { onAction(ProfileAction.SetFeelAndEffort(it)) },
            modifier = Modifier.testTag("feel_and_effort_toggle"),
        )
        Toggle(
            label = "Training effect",
            checked = state.profile.trainingEffect,
            onChange = { onAction(ProfileAction.SetTrainingEffect(it)) },
            modifier = Modifier.testTag("training_effect_toggle"),
        )
    }
}
