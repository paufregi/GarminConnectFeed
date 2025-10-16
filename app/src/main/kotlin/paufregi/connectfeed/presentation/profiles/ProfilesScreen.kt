package paufregi.connectfeed.presentation.profiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
            itemsIndexed(state.profiles) { index, it ->
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
                        ActivityIcon(it.activityType)
                        Text(it.name)
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            icon = Icons.Default.Delete,
                            onClick = { onAction(ProfileAction.Delete(it)) },
                            modifier = Modifier.testTag("delete_profile_${it.id}")
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
