package paufregi.connectfeed.presentation.app.activities.quickedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.EffortSlider
import paufregi.connectfeed.presentation.ui.components.FeelSelector
import paufregi.connectfeed.presentation.ui.components.cards.ActivityCard
import paufregi.connectfeed.presentation.ui.components.toDropdownItem
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Strava
import paufregi.connectfeed.presentation.ui.utils.add
import paufregi.connectfeed.presentation.ui.utils.launchGarmin
import paufregi.connectfeed.presentation.ui.utils.launchStrava

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

    when(val s = state.status) {
        is Status.Loading -> Loading()
        is Status.Success -> Success(
            activity = state.activity,
            navBack = navBack
        )
        is Status.Failure -> Failure(
            reason = s.reason,
            onReset = { viewModel.onAction(QuickEditAction.ResetStatus) }
        )
        else -> Form(
            state = state,
            onAction = viewModel::onAction,
            navBack = navBack,
            navEdit = navEdit
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun Frame(
    navBack: () -> Unit = {},
    enableNavBack: Boolean = true,
    navEdit: (() -> Unit)? = null,
    enableNavEdit: Boolean = true,
    bottomContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Quick Edit activity",
                        modifier = Modifier.padding(start = 20.dp)
                    )
                },
                navigationIcon = {
                    Button(
                        modifier = Modifier.testTag("nav_back"),
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = navBack,
                        enabled = enableNavBack
                    )
                },
                actions = {
                    navEdit?.let {
                        Button(
                            modifier = Modifier.testTag("nav_edit"),
                            icon = Icons.Default.Edit,
                            onClick = it,
                            enabled = enableNavEdit
                        )
                    }
                }
            )
        },
        bottomBar = bottomContent,
        content = content,
    )
}

object Frame {
    @Composable
    fun saveButton(onClick: () -> Unit): @Composable () -> Unit = {
        BottomAppBar {
            Button(
                text = "Save",
                modifier = Modifier.fillMaxWidth().testTag("save_button"),
                onClick = onClick
            )
        }
    }

    @Composable
    fun okButton(onClick: () -> Unit): @Composable () -> Unit = {
        BottomAppBar {
            Button(
                text = "OK",
                modifier = Modifier.fillMaxWidth().testTag("ok_button"),
                onClick = onClick
            )
        }
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
    Frame(
        navBack = navBack,
        navEdit = navEdit,
        bottomContent = Frame.saveButton({ onAction(QuickEditAction.Save) })
    ) { padding ->
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

                if (state.profile != null) {
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

                    if (state.profile.customWater) {
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
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun Success(
    @PreviewParameter(QuickEditSuccessPreview::class) activity: Activity,
    navBack: () -> Unit = {},
) {
    val context = LocalContext.current

    Frame(
        enableNavBack = false,
        enableNavEdit = false,
        bottomContent = Frame.okButton(navBack)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(20.dp))
                .testTag("edit_success")
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                tint = Color.Green,
                modifier = Modifier
                    .size(110.dp)
                    .testTag("icon")
            )
            Text(
                text = "Update completed!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .testTag("title")
            )
            Button(
                text = "Open Garmin Connect",
                icon = Icons.Connect.Logo,
                onClick = { launchGarmin(context, activity.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("garmin")
            )
            activity.stravaId?.let {
                Button(
                    text = "Open Strava",
                    icon = Icons.Strava.Logo,
                    onClick = { launchStrava(context, activity.stravaId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("strava")
                )
            }
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun Failure(
    reason: String = "Unknown issue",
    onReset: () -> Unit = {},
) {
    Frame(
        enableNavBack = false,
        enableNavEdit = false,
        bottomContent = Frame.okButton(onReset)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(20.dp))
                .testTag("edit_failure")
        ) {
            Icon(
                imageVector = Icons.Filled.Cancel,
                contentDescription = "Failure",
                tint = Color.Red,
                modifier = Modifier
                    .size(110.dp)
                    .testTag("icon")
            )
            Text(
                text = "Update failed!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .testTag("title")
            )
            Text(
                text = "Something went wrong: $reason",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("reason")
            )
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun Loading() {
    Frame(
        enableNavBack = false,
        enableNavEdit = false,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(horizontal = 20.dp))
                .testTag("edit_loading")
        ) {
            CircularProgressIndicator(
                strokeWidth = 8.dp,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

