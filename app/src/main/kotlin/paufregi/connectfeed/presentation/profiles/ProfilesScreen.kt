package paufregi.connectfeed.presentation.profiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.ui.components.ActivityIcon
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold

@Composable
@ExperimentalMaterial3Api
internal fun ProfilesScreen(
    nav: NavHostController = rememberNavController(),
) {
    val viewModel = hiltViewModel<ProfilesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationScaffold(
        items = Navigation.items,
        selectedIndex = Navigation.PROFILES,
        nav = nav,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { nav.navigate(Route.Profile()) },
                modifier = Modifier.testTag("create_profile")
            ) { Icon(Icons.Default.Add, "Create profile") }
        }
    ) {
        ProfilesContent(state, viewModel::onEvent, nav)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ProfilesContent(
    @PreviewParameter(ProfilesStatePreview::class) state: ProfilesState,
    onEvent: (ProfileEvent) -> Unit = {},
    nav: NavHostController = rememberNavController(),
    paddingValues: PaddingValues = PaddingValues(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 40.dp)
            .verticalScroll(rememberScrollState())
            .testTag("profiles_content")
    ) {
        if (state.profiles.isEmpty()) {
            Text("No profile")
        }
        state.profiles.fastForEachIndexed { index, it ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(50.dp)
                    .clickable(onClick = { nav.navigate(Route.Profile(it.id)) })
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(10.dp),
                ) {
                    ActivityIcon(it.activityType)
                    Text(it.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        icon = Icons.Default.Delete,
                        onClick = { onEvent(ProfileEvent.Delete(it)) },
                        modifier = Modifier.testTag("delete_profile_${it.id}")
                    )
                }
            }
        }
    }
}
