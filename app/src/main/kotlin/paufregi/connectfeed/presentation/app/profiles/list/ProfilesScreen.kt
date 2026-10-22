package paufregi.connectfeed.presentation.app.profiles.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.ui.components.cards.ProfileCard
import paufregi.connectfeed.presentation.ui.components.frame.AppFrame
import paufregi.connectfeed.presentation.ui.components.loading.AppLoading
import paufregi.connectfeed.presentation.ui.utils.add

private const val TITLE = "Profiles"

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun ProfilesScreen(
    user: User,
    navigate: (AppRoute) -> Unit = {},
    onOpen: (Long) -> Unit,
    onCreate: () -> Unit,
    viewModel: ProfilesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfilesContent(state, user, navigate, onOpen, onCreate, viewModel::onAction)
}

@Composable
@ExperimentalMaterial3Api
internal fun ProfilesContent(
    state: ProfilesState,
    user: User,
    navigate: (AppRoute) -> Unit = {},
    onOpen: (Long) -> Unit = {},
    onCreate: () -> Unit = {},
    onAction: (ProfilesAction) -> Unit = {},
) {
    when (state.isLoading) {
        true -> AppLoading(TITLE, AppRoute.Profiles)
        false -> ProfileList(state, user, navigate, onOpen, onCreate, onAction)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun ProfileList(
    @PreviewParameter(ProfilesStatePreview::class) state: ProfilesState,
    user: User = User.ANONYM,
    navigate: (AppRoute) -> Unit = {},
    onOpen: (Long) -> Unit = {},
    onCreate: () -> Unit = {},
    onAction: (ProfilesAction) -> Unit = {},
) {
    AppFrame(
        title = TITLE,
        user = user,
        currentRoute = AppRoute.Profiles,
        navigate = navigate,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate,
                modifier = Modifier.testTag("create_profile")
            ) { Icon(Icons.Default.Add, "Create profile") }
        }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = padding.add(horizontal = 20.dp),
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .testTag("profile_list_content")
        ) {
            when (state.profiles.isEmpty()) {
                true -> item { Text("No profiles", modifier = Modifier.testTag("no_profiles")) }
                false -> items(state.profiles, key = { it.id }) { profile ->
                    ProfileCard(
                        profile = profile,
                        modifier = Modifier.fillMaxWidth().testTag("profile_${profile.id}"),
                        onClick = { onOpen(profile.id) },
                        onDelete = { onAction(ProfilesAction.Delete(profile)) }
                    )
                }
            }
        }
    }
}
