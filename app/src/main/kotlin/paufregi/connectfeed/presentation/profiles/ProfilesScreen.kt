package paufregi.connectfeed.presentation.profiles

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.ConfirmationDialog
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.utils.iconFor

@Composable
@ExperimentalMaterial3Api
internal fun ProfilesScreen(
    nav: NavHostController = rememberNavController(),
) {
    val viewModel = hiltViewModel<ProfilesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationScaffold(
        topItems = Navigation.topItems,
        bottomItems = Navigation.bottomItems,
        selectedIndex = Navigation.PROFILES,
        nav = nav,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { nav.navigate(Route.Profile()) },
                modifier = Modifier.testTag("create_profile")
            ) { Icon(Icons.Default.Add, "Create profile") }
        }
    ) {
        ProfilesContent(state, viewModel::onAction, nav, it)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ProfilesContent(
    @PreviewParameter(ProfilesStatePreview::class) state: ProfilesState,
    onAction: (ProfileAction) -> Unit = {},
    nav: NavHostController = rememberNavController(),
    paddingValues: PaddingValues = PaddingValues(),
) {
    var profileToDelete by remember { mutableStateOf<Profile?>(null) }

    profileToDelete?.let {
        ConfirmationDialog(
            title = "Delete profile",
            message = "Are you sure you want to delete this ${it.name} profile?",
            onConfirm = { onAction(ProfileAction.Delete(it)) },
            onDismiss = { profileToDelete = null },
            modifier = Modifier.testTag("delete_dialog")
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr) + 20.dp,
                end = paddingValues.calculateRightPadding(LayoutDirection.Ltr) + 20.dp,
            )
            .testTag("profiles_content")
    ) {
        if (state.profiles.isEmpty()) {
            item { Text("No profile") }
        } else {
            itemsIndexed(state.profiles) { _, it ->
                Card(
                    modifier = Modifier
                        .height(50.dp)
                        .clickable(onClick = { nav.navigate(Route.Profile(it.id)) })
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(10.dp),
                    ) {
                        iconFor(it.type)?.let { i -> Icon(i, i.name, Modifier.size(24.dp)) }
                        Text(
                            text = it.name,
                            modifier = Modifier
                                .weight(1f)
                                .basicMarquee(),
                            maxLines = 1
                        )
                        Button(
                            icon = Icons.Default.Delete,
                            onClick = { profileToDelete = it },
                            modifier = Modifier.testTag("delete_profile_${it.id}")
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
