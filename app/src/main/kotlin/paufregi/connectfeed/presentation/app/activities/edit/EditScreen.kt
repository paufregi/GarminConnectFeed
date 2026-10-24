package paufregi.connectfeed.presentation.app.activities.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.EffortSlider
import paufregi.connectfeed.presentation.ui.components.FeelSelector
import paufregi.connectfeed.presentation.ui.components.Toggle
import paufregi.connectfeed.presentation.ui.components.cards.ActivityCard
import paufregi.connectfeed.presentation.ui.components.frame.FormFrame
import paufregi.connectfeed.presentation.ui.components.frame.OkBar
import paufregi.connectfeed.presentation.ui.components.frame.SaveBar
import paufregi.connectfeed.presentation.ui.components.status.ActivityLinks
import paufregi.connectfeed.presentation.ui.components.status.FailureMessage
import paufregi.connectfeed.presentation.ui.components.status.SuccessMessage
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.icons.app.App
import paufregi.connectfeed.presentation.ui.utils.add

private const val TITLE = "Edit activity"

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun EditScreen(
    id: Long,
    stravaId: Long?,
    navBack: () -> Unit = {},
    navQuickEdit: () -> Unit = {},
) {
    val viewModel = hiltViewModel<EditViewModel, EditViewModel.Factory>(
        creationCallback = { it.create(id, stravaId) }
    )

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect is EditEffect.NavigateBack) navBack()
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    EditContent(
        state = state,
        onAction = viewModel::onAction,
        navBack = navBack,
        navQuickEdit = navQuickEdit,
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun EditContent(
    state: EditState,
    onAction: (EditAction) -> Unit = {},
    navBack: () -> Unit = {},
    navQuickEdit: () -> Unit = {},
) {
    when (val s = state.status) {
        is Status.Loading -> FormFrame(TITLE, action = Icons.App.QuickEdit, loading = true)
        is Status.Success -> FormFrame(
            title = TITLE,
            action = Icons.App.QuickEdit,
            enabled = false,
            bottomBar = { OkBar(navBack) },
        ) { padding ->
            SuccessMessage("Update completed!", padding) { ActivityLinks(state.activity) }
        }
        is Status.Failure -> FormFrame(
            title = TITLE,
            action = Icons.App.QuickEdit,
            enabled = false,
            bottomBar = { OkBar { onAction(EditAction.ResetStatus) } },
        ) { padding ->
            FailureMessage("Update failed!", s.reason, padding)
        }
        else -> Form(
            state = state,
            onAction = onAction,
            navBack = navBack,
            navQuickEdit = navQuickEdit,
        )
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun Form(
    @PreviewParameter(EditPreview::class) state: EditState,
    onAction: (EditAction) -> Unit = {},
    navBack: () -> Unit = {},
    navQuickEdit: () -> Unit = {},
) {
    FormFrame(
        title = TITLE,
        navBack = navBack,
        action = Icons.App.QuickEdit,
        onAction = navQuickEdit,
        bottomBar = { SaveBar { onAction(EditAction.Save) } },
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .padding(padding.add(horizontal = 20.dp))
                .testTag("edit_form")
        ) {
            ActivityCard(
                activity = state.activity,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("activity_${state.activity.id}"),
                onClick = { }
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
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
                if (state.availableCourses.isNotEmpty()) {
                    Dropdown(
                        label = { Text("Course") },
                        selected = state.course?.toDropdownItem { },
                        modifier = Modifier.fillMaxWidth(),
                        items = state.availableCourses
                            .map { it.toDropdownItem { onAction(EditAction.SetCourse(it)) } }
                    )
                }
                if (state.availableGears.isNotEmpty()) {
                    Dropdown(
                        label = { Text("Gear") },
                        selected = state.gear?.toDropdownItem { },
                        modifier = Modifier.fillMaxWidth(),
                        items = state.availableGears
                            .map { it.toDropdownItem { onAction(EditAction.SetGear(it)) } }
                    )
                }
                state.activity.stravaId?.let {
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
                state.activity.stravaId?.let {
                    Toggle(
                        label = "Training effect",
                        checked = state.trainingEffect,
                        onChange = { onAction(EditAction.SetTrainingEffect(it)) },
                    )
                }
            }
        }
    }
}
