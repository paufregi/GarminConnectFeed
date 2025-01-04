package paufregi.connectfeed.presentation.quickedit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.CustomSlider
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.IconRadioGroup
import paufregi.connectfeed.presentation.ui.components.IconRadioItem
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.StatusInfo
import paufregi.connectfeed.presentation.ui.components.StatusInfoType
import paufregi.connectfeed.presentation.ui.components.TextEffort
import paufregi.connectfeed.presentation.ui.components.TextFeel
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
internal fun QuickEditScreen(nav: NavController = rememberNavController()) {
    val viewModel = hiltViewModel<QuickEditViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    QuickEditContent(state, viewModel::onEvent, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun QuickEditContent(
    @PreviewParameter(QuickEditStatePreview ::class) state: QuickEditState,
    onEvent: (QuickEditEvent) -> Unit = {},
    nav: NavController = rememberNavController()
) {
    when (state.process) {
        is ProcessState.Processing -> SimpleScaffold { Loading(it) }
        is ProcessState.Success -> SimpleScaffold {
            StatusInfo(
                type = StatusInfoType.Success,
                text = state.process.message,
                actionButton = { Button(text = "Ok", onClick = { onEvent(QuickEditEvent.Restart) }) },
                paddingValues = it
            )
        }
        is ProcessState.Failure -> SimpleScaffold {
            StatusInfo(
                type = StatusInfoType.Failure,
                text = state.process.reason,
                actionButton = { Button(text = "Ok", onClick = { onEvent(QuickEditEvent.Restart) }) },
                paddingValues = it
            )
        }
        is ProcessState.Idle -> NavigationScaffold(
            items = Navigation.items,
            selectedIndex = Navigation.HOME,
            nav = nav
        ) { QuickEditForm(state, onEvent, it) }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun QuickEditForm(
    @PreviewParameter(QuickEditStatePreview ::class) state: QuickEditState,
    onEvent: (QuickEditEvent) -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(),
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
            .testTag("quick_edit_form")
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        Dropdown(
            label = { Text("Activity") },
            selected = state.activity?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.activities.map {
                it.toDropdownItem {
                    onEvent(
                        QuickEditEvent.SetActivity(
                            it
                        )
                    )
                }
            }
        )
        Dropdown(
            label = { Text("Profile") },
            selected = state.profile?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.profiles.filter { state.activity?.type == null || it.activityType == state.activity.type } .map {
                it.toDropdownItem {
                    onEvent(
                        QuickEditEvent.SetProfile(
                            it
                        )
                    )
                }
            }
        )
        if (state.profile?.customWater == true) {
            TextField(
                label = { Text("Water") },
                value = state.profile.water?.toString() ?: "",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { if (it.isDigitsOnly()) onEvent(QuickEditEvent.SetWater(it.toInt())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        if (state.profile?.feelAndEffort == true) {
            Column {
                IconRadioGroup(
                    options = listOf(
                        IconRadioItem(0f, Icons.Filled.MoodBad),
                        IconRadioItem(25f, Icons.Filled.SentimentVeryDissatisfied),
                        IconRadioItem(50f, Icons.Filled.SentimentNeutral),
                        IconRadioItem(75f, Icons.Filled.SentimentSatisfiedAlt),
                        IconRadioItem(100f, Icons.Filled.Mood),
                    ),
                    selected = state.feel,
                    onClick = { onEvent(QuickEditEvent.SetFeel(it)) }
                )
                TextFeel(
                    state.feel,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(vertical = 10.dp)
                )
            }
            Column {
                Slider(
                    value = state.effort ?: 0f,
                    onValueChange = { onEvent(QuickEditEvent.SetEffort(it.toInt().toFloat())) },
                    valueRange = 0f..100f,
                    steps = 9,
                    interactionSource = interactionSource,
                    track = CustomSlider.track,
                    thumb = CustomSlider.thumb(interactionSource),
                    modifier = Modifier.fillMaxWidth()
                )
                TextEffort(
                    state.effort?: 0f,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                text = "Save",
                enabled = state.activity != null && state.profile != null,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onEvent(QuickEditEvent.Save)
                }
            )
        }
    }
}
