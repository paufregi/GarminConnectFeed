package paufregi.connectfeed.presentation.modify

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.presentation.HomeNavigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.CustomSlider
import paufregi.connectfeed.presentation.ui.components.Dropdown
import paufregi.connectfeed.presentation.ui.components.IconRadioGroup
import paufregi.connectfeed.presentation.ui.components.IconRadioItem
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
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
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.ui.utils.iconFor
import paufregi.connectfeed.presentation.ui.utils.launchGarmin
import paufregi.connectfeed.presentation.ui.utils.launchStrava

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun ModifyScreen(nav: NavHostController = rememberNavController()) {
    val viewModel = hiltViewModel<ModifyViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ModifyContent(state, viewModel::onAction, nav)
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ModifyContent(
    @PreviewParameter(ModifyStatePreview::class) state: ModifyState,
    onAction: (ModifyAction) -> Unit = {},
    nav: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current

    when (val process = state.process) {
        is ProcessState.Processing -> SimpleScaffold { Loading(it) }
        is ProcessState.Success -> SimpleScaffold {
            successActivityUpdate(
                action = { onAction(ModifyAction.Restart) },
                garmin = { launchGarmin(context, state.activity) },
                strava = { launchStrava(context, state.stravaActivity) },
            )(process, it)
        }
        is ProcessState.Failure -> SimpleScaffold {
            failureInfo { onAction(ModifyAction.Restart) }(process, it)
        }
        is ProcessState.Idle -> NavigationScaffold(
            topItems = paufregi.connectfeed.presentation.Navigation.topItems,
            bottomItems = paufregi.connectfeed.presentation.Navigation.bottomItems,
            bottomBar = {
                paufregi.connectfeed.presentation.ui.components.NavigationBar(
                    items = HomeNavigation.items,
                    selectedIndex = HomeNavigation.MODIFY.barIndex,
                    nav = nav,
                )
            },
            selectedIndex = HomeNavigation.MODIFY.menuIndex,
            nav = nav,
        ) { paddingValues ->
            AnimatedContent(
                targetState = state.activity,
                label = "modify-selection",
            ) { activity ->
                if (activity == null) {
                    ActivityList(
                        activities = state.activities,
                        paddingValues = paddingValues,
                        onActivityClick = { onAction(ModifyAction.SetActivity(it)) },
                    )
                } else {
                    ModifyDetails(
                        state = state,
                        activity = activity,
                        paddingValues = paddingValues,
                        onAction = onAction,
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityList(
    activities: List<Activity>,
    paddingValues: PaddingValues,
    onActivityClick: (Activity) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr) + 20.dp,
                end = paddingValues.calculateRightPadding(LayoutDirection.Ltr) + 20.dp,
            )
            .testTag("modify_activity_list"),
    ) {
        if (activities.isEmpty()) {
            item { Text("No activities") }
        } else {
            items(activities, key = { it.id }) { activity ->
                ActivityCard(
                    activity = activity,
                    modifier = Modifier.testTag("modify_activity_${activity.id}"),
                    onClick = { onActivityClick(activity) },
                )
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ModifyDetails(
    state: ModifyState,
    activity: Activity,
    paddingValues: PaddingValues,
    onAction: (ModifyAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr) + 20.dp,
                end = paddingValues.calculateRightPadding(LayoutDirection.Ltr) + 20.dp,
            )
            .testTag("modify_detail"),
    ) {
        item {
            ActivityCard(activity = activity, onClick = {})
        }
        item {
            ModeSwitch(
                mode = state.mode,
                onCheckedChange = {
                    onAction(
                        ModifyAction.SetMode(
                            if (it) ModifyMode.Profile else ModifyMode.Manual,
                        ),
                    )
                },
            )
        }
        item {
            when (state.mode) {
                ModifyMode.Manual -> ManualForm(
                    state = state,
                    interactionSource = interactionSource,
                    onAction = onAction,
                )
                ModifyMode.Profile -> ProfileForm(
                    state = state,
                    interactionSource = interactionSource,
                    onAction = onAction,
                )
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            ) {
                Button(
                    text = "Back",
                    onClick = { onAction(ModifyAction.ClearActivity) },
                )
                Button(
                    text = "Save",
                    enabled = state.canSave,
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onAction(ModifyAction.Save)
                    },
                )
            }
        }
    }
}

@Composable
private fun ActivityCard(
    activity: Activity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            iconFor(activity.type).let { icon ->
                Icon(icon, icon.name, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.name,
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = activity.date?.let { Formatter.dateTime(it) } ?: "No date",
                        fontSize = 11.sp,
                    )
                    activity.distance?.let {
                        Text(text = "${Formatter.distance(it)} km", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ModeSwitch(
    mode: ModifyMode,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().testTag("modify_mode_switch"),
    ) {
        Text("Manual")
        Switch(
            checked = mode == ModifyMode.Profile,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(horizontal = 12.dp),
        )
        Text("Profile")
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ManualForm(
    state: ModifyState,
    interactionSource: MutableInteractionSource,
    onAction: (ModifyAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        TextField(
            label = { Text("Name") },
            value = state.name ?: "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onAction(ModifyAction.SetName(it)) },
        )
        Dropdown(
            label = { Text("Event type") },
            selected = state.eventType?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = state.eventTypes.map { it.toDropdownItem { onAction(ModifyAction.SetEventType(it)) } },
        )
        state.activity?.takeIf { it.type.allowCourse }?.let { activity ->
            Dropdown(
                label = { Text("Course") },
                selected = state.course?.toDropdownItem { },
                modifier = Modifier.fillMaxWidth(),
                items = state.courses
                    .filter { it.type.compatible(activity.type) }
                    .map { it.toDropdownItem { onAction(ModifyAction.SetCourse(it)) } },
            )
        }
        state.activity?.let { activity ->
            state.gears
                .filter { it.type.compatible(activity.type) }
                .takeIf { it.isNotEmpty() }
                ?.let { compatibleGears ->
                    Dropdown(
                        label = { Text("Gear") },
                        selected = state.gear?.toDropdownItem { },
                        modifier = Modifier.fillMaxWidth(),
                        items = compatibleGears.map { it.toDropdownItem { onAction(ModifyAction.SetGear(it)) } },
                    )
                }
        }
        if (state.stravaActivity != null) {
            TextField(
                label = { Text("Description") },
                value = state.description ?: "",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onAction(ModifyAction.SetDescription(it)) },
            )
        }
        TextField(
            label = { Text("Water") },
            value = state.water?.toString() ?: "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onAction(ModifyAction.SetWater(it.toIntOrNull())) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
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
                onClick = { onAction(ModifyAction.SetFeel(it)) },
            )
            TextFeel(
                state.feel,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp)
                    .testTag("modify_feel_text"),
            )
        }
        Column {
            Slider(
                value = state.effort ?: 0f,
                onValueChange = { onAction(ModifyAction.SetEffort(it.toInt().toFloat())) },
                valueRange = 0f..100f,
                steps = 9,
                interactionSource = interactionSource,
                track = CustomSlider.track,
                thumb = CustomSlider.thumb(interactionSource),
                modifier = Modifier.fillMaxWidth(),
            )
            TextEffort(
                state.effort ?: 0f,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag("modify_effort_text"),
            )
        }
        if (state.stravaActivity != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { onAction(ModifyAction.SetTrainingEffect(!state.trainingEffect)) },
                    ),
            ) {
                Checkbox(
                    modifier = Modifier.testTag("modify_training_effect_checkbox"),
                    checked = state.trainingEffect,
                    onCheckedChange = { onAction(ModifyAction.SetTrainingEffect(it)) },
                )
                Text(text = "Training effect")
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ProfileForm(
    state: ModifyState,
    interactionSource: MutableInteractionSource,
    onAction: (ModifyAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        val compatibleProfiles = state.activity?.let { activity ->
            state.profiles.filter { it.type.compatible(activity.type) }
        }.orEmpty()

        Dropdown(
            label = { Text("Profile") },
            selected = state.profile?.toDropdownItem { },
            modifier = Modifier.fillMaxWidth(),
            items = compatibleProfiles.map { it.toDropdownItem { onAction(ModifyAction.SetProfile(it)) } },
        )

        val selectedProfile: Profile = state.profile ?: return@Column

        TextField(
            label = { Text("Description") },
            value = state.description ?: "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onAction(ModifyAction.SetDescription(it)) },
        )

        state.activity?.let { activity ->
            if (selectedProfile.gear) {
                state.gears
                    .filter { it.type.compatible(activity.type) }
                    .takeIf { it.isNotEmpty() }
                    ?.let { compatibleGears ->
                        Dropdown(
                            label = { Text("Gear") },
                            selected = state.gear?.toDropdownItem { },
                            modifier = Modifier.fillMaxWidth(),
                            items = compatibleGears.map { it.toDropdownItem { onAction(ModifyAction.SetGear(it)) } },
                        )
                    }
            }
        }

        if (selectedProfile.customWater) {
            TextField(
                label = { Text("Water") },
                value = state.water?.toString() ?: "",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onAction(ModifyAction.SetWater(it.toIntOrNull())) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        if (selectedProfile.feelAndEffort) {
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
                    onClick = { onAction(ModifyAction.SetFeel(it)) },
                )
                TextFeel(
                    state.feel,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 10.dp),
                )
            }
            Column {
                Slider(
                    value = state.effort ?: 0f,
                    onValueChange = { onAction(ModifyAction.SetEffort(it.toInt().toFloat())) },
                    valueRange = 0f..100f,
                    steps = 9,
                    interactionSource = interactionSource,
                    track = CustomSlider.track,
                    thumb = CustomSlider.thumb(interactionSource),
                    modifier = Modifier.fillMaxWidth(),
                )
                TextEffort(
                    state.effort ?: 0f,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}
