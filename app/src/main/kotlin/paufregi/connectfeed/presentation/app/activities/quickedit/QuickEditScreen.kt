package paufregi.connectfeed.presentation.app.activities.quickedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import paufregi.connectfeed.presentation.ui.components.cards.ActivityCard
import paufregi.connectfeed.presentation.ui.components.cards.ProfileCard
import paufregi.connectfeed.presentation.ui.components.frame.FormFrame
import paufregi.connectfeed.presentation.ui.components.frame.OkBar
import paufregi.connectfeed.presentation.ui.components.frame.SaveBar
import paufregi.connectfeed.presentation.ui.components.status.ActivityLinks
import paufregi.connectfeed.presentation.ui.components.status.FailureMessage
import paufregi.connectfeed.presentation.ui.components.status.SuccessMessage
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.icons.app.App
import paufregi.connectfeed.presentation.ui.utils.add

private const val TITLE = "Quick edit activity"

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun QuickEditScreen(
    id: Long,
    stravaId: Long?,
    navBack: () -> Unit = {},
    navEdit: () -> Unit = {},
) {
    val viewModel = hiltViewModel<QuickEditViewModel, QuickEditViewModel.Factory>(
        creationCallback = { it.create(id, stravaId) }
    )

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect is QuickEditEffect.NavigateBack) navBack()
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    QuickEditContent(
        state = state,
        onAction = viewModel::onAction,
        navBack = navBack,
        navEdit = navEdit,
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun QuickEditContent(
    state: QuickEditState,
    onAction: (QuickEditAction) -> Unit = {},
    navBack: () -> Unit = {},
    navEdit: () -> Unit = {},
) {
    when (val s = state.status) {
        is Status.Loading -> FormFrame(TITLE, action = Icons.App.Edit, loading = true)
        is Status.Success -> FormFrame(
            title = TITLE,
            action = Icons.App.Edit,
            enabled = false,
            bottomBar = { OkBar(navBack) },
        ) { padding ->
            SuccessMessage("Update completed!", padding) { ActivityLinks(state.activity) }
        }
        is Status.Failure -> FormFrame(
            title = TITLE,
            action = Icons.App.Edit,
            enabled = false,
            bottomBar = { OkBar { onAction(QuickEditAction.ResetStatus) } },
        ) { padding ->
            FailureMessage("Update failed!", s.reason, padding)
        }
        else -> Form(
            state = state,
            onAction = onAction,
            navBack = navBack,
            navEdit = navEdit
        )
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun Form(
    @PreviewParameter(QuickEditPreview::class) state: QuickEditState,
    onAction: (QuickEditAction) -> Unit = {},
    navBack: () -> Unit = {},
    navEdit: () -> Unit = {},
) {
    val navAction = state.profile?.let { { onAction(QuickEditAction.SetProfile(null)) } } ?: { navBack() }
    FormFrame(
        title = TITLE,
        navBack = navAction,
        action = Icons.App.Edit,
        onAction = navEdit,
        bottomBar = { SaveBar(state.canSave) { onAction(QuickEditAction.Save) } },
    ) { padding ->
        if(state.profile == null) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = padding.add(horizontal = 20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(padding)
                    .testTag("profile_list_content")
            ) {
                when (state.availableProfiles.isEmpty()) {
                    true -> item { Text("No profiles", modifier = Modifier.testTag("no_profiles")) }
                    false -> items(state.availableProfiles, key = { it.id }) { profile ->
                        ProfileCard(
                            profile = profile,
                            modifier = Modifier.fillMaxWidth().testTag("profile_${profile.id}"),
                            onClick = { onAction(QuickEditAction.SetProfile(profile)) },
                            onDelete = { onAction(QuickEditAction.SetProfile(null)) }
                        )
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(padding)
                    .padding(padding.add(horizontal = 20.dp))
                    .testTag("quick_edit_form")
            ) {
                ActivityCard(
                    activity = state.activity,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("activity_${state.activity.id}"),
                )

                ProfileCard(
                    profile = state.profile,
                    onDelete = { onAction(QuickEditAction.SetProfile(null)) },
                    deleteIcon = Icons.Default.Clear,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_${state.profile.id}"),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (state.profile.gear && state.availableGears.isNotEmpty()) {
                        Dropdown(
                            label = { Text("Gear") },
                            selected = state.gear?.toDropdownItem { },
                            modifier = Modifier.fillMaxWidth(),
                            items = state.availableGears
                                .map { it.toDropdownItem { onAction(QuickEditAction.SetGear(it)) } }
                        )
                    }
                    state.activity.stravaId?.let {
                        TextField(
                            label = { Text("Description") },
                            value = state.description ?: "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            onValueChange = { onAction(QuickEditAction.SetDescription(it)) }
                        )
                    }
                    if (state.profile.customWater) {
                        TextField(
                            label = { Text("Water") },
                            value = state.water?.toString() ?: "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            onValueChange = { onAction(QuickEditAction.SetWater(it.toIntOrNull())) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    if (state.profile.feelAndEffort) {
                        FeelSelector(
                            value = state.feel,
                            onChange = { onAction(QuickEditAction.SetFeel(it)) }
                        )

                        EffortSlider(
                            value = state.effort,
                            onChange = { onAction(QuickEditAction.SetEffort(it)) }
                        )
                    }
                }
            }
        }
    }
}
