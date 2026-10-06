package paufregi.connectfeed.presentation.app.activities.quickedit

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.EffortSlider
import paufregi.connectfeed.presentation.ui.components.FeelSelector
import paufregi.connectfeed.presentation.ui.components.cards.ActivityCard
import paufregi.connectfeed.presentation.ui.components.screens.BackScreen
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun QuickEditScreen(
    id: Long,
    stravaId: Long?,
    onBack: () -> Unit = {},
    toEdit: () -> Unit = {},
) {
    val viewModel = hiltViewModel<QuickEditViewModel, QuickEditViewModel.Factory>(
        creationCallback = { it.create(id, stravaId) }
    )

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect is QuickEditEffect.NavigateBack) onBack()
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    BackScreen(
        title = "Edit activity",
        isLoading = state.loading,
        navigateBack = onBack,
        actions = {
            BackScreen.Navigate(
                icon = Icons.Default.Edit,
                enabled = !state.loading,
                onClick = toEdit,
            )
        },
        bottomBarContent = {
            Button(
                text = "Save activity",
                modifier = Modifier.fillMaxWidth().testTag("save"),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    viewModel.onAction(QuickEditAction.Save)
                }
            )
        }
    ) { EditForm(state, viewModel::onAction, it) }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun EditForm(
    @PreviewParameter(QuickEditPreview::class) state: QuickEditState,
    onAction: (QuickEditAction) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(padding)
            .padding(padding.add(horizontal = 20.dp))
            .testTag("activity_form_content")
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
            Dropdown(
                label = { Text("Profile") },
                selected = state.profile?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.availableProfiles
                    .map { it.toDropdownItem { onAction(QuickEditAction.SetProfile(it)) } },
            )

            if(state.profile != null) {
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
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        onValueChange = { onAction(QuickEditAction.SetDescription(it)) }
                    )
                }

                if(state.profile.customWater) {
                    TextField(
                        label = { Text("Water") },
                        value = state.water?.toString() ?: "",
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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

