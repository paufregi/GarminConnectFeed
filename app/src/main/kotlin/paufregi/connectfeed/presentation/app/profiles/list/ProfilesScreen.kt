package paufregi.connectfeed.presentation.app.profiles.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
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
import paufregi.connectfeed.presentation.ui.components.ProfileCard
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun ProfilesScreen(
    onOpen: (Long) -> Unit,
    onCreate: () -> Unit,
    padding: PaddingValues = PaddingValues(),
    viewModel: ProfilesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProfileList(state, viewModel::onAction, onOpen, onCreate, padding)
}


@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ProfileList(
    @PreviewParameter(ProfilesStatePreview ::class) state: ProfilesState,
    onAction: (ProfilesAction) -> Unit = {},
    onOpen: (Long) -> Unit = {},
    onCreate: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreate() },
                modifier = Modifier.testTag("create_profile")
            ) { Icon(Icons.Default.Add, "Create profile") }
        },
        modifier = Modifier.fillMaxSize().testTag("profile_list_content")
    ) { innerPadding ->
        val consumedInsets = padding.add(innerPadding)
        val contentPadding = padding.add(innerPadding).add(horizontal = 20.dp)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = contentPadding,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(consumedInsets)
        ) {
            if (state.profiles.isEmpty()) {
                item { Text("No profiles", modifier = Modifier.testTag("no_profiles")) }
            } else {
                items(state.profiles, key = { it.id }) { profile ->
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
