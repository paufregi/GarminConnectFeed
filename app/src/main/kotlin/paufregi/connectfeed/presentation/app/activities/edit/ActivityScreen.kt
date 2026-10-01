package paufregi.connectfeed.presentation.app.activities.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.ui.components.ActivityCard
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun ActivityScreen(
    id: Long,
    stravaId: Long,
    onBack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {

    val viewModel = hiltViewModel<ActivityViewModel, ActivityViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(id, stravaId)
        }
    )

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect is ActivityEffect.NavigateBack) onBack()
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.loading) {
        true -> Loading()
        false -> ActivityForm(state, viewModel::onAction, padding)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ActivityForm(
    @PreviewParameter(ActivityPreview::class) state: ActivityState,
    onAction: (ActivityAction) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().testTag("activity_form_content")
    ) { innerPadding ->
        val consumedInsets = padding.add(innerPadding)
        val contentPadding = padding.add(innerPadding).add(horizontal = 20.dp)

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .consumeWindowInsets(consumedInsets)
                .padding(contentPadding)
        ) {
            ActivityCard(
                activity = state.activity,
                modifier = Modifier.fillMaxWidth().testTag("activity_${state.activity.id}"),
                onClick = { }
            )

            TextField(
                label = { Text("Name") },
                value = state.name ?: "",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                onValueChange = { onAction(ActivityAction.SetName(it)) }
            )

            Dropdown(
                label = { Text("Event type") },
                selected = state.eventType?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.eventTypes
                    .map { it.toDropdownItem { onAction(ActivityAction.SetEventType(it)) } },
            )

            if (state.activity.type.allowCourse) {
                Dropdown(
                    label = { Text("Course") },
                    selected = state.course?.toDropdownItem { },
                    modifier = Modifier.fillMaxWidth(),
                    items = state.courses
                        .filter { it.type.compatible(state.activity.type) }
                        .map { it.toDropdownItem { onAction(ActivityAction.SetCourse(it)) } }
                )
            }

            if (state.gears.any { it.type.compatible(state.activity.type) }) {
                Dropdown(
                    label = { Text("Gear") },
                    selected = state.gear?.toDropdownItem { },
                    modifier = Modifier.fillMaxWidth(),
                    items = state.gears
                        .filter { it.type.compatible(state.activity.type) }
                        .map { it.toDropdownItem { onAction(ActivityAction.SetGear(it)) } }
                )
            }

            state.activity.stravaId?.let {
                TextField(
                    label = { Text("Description") },
                    value = state.description ?: "",
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    onValueChange = { onAction(ActivityAction.SetDescription(it)) }
                )
            }


        }
    }
}

