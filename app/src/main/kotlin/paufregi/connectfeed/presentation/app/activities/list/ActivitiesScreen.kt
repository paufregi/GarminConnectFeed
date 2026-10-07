package paufregi.connectfeed.presentation.app.activities.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.NavigationDrawer
import paufregi.connectfeed.presentation.ui.components.cards.ActivityCard
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun ActivitiesScreen(
    user: User,
    navigate: (AppRoute) -> Unit = {},
    onOpen: (Long, Long?) -> Unit = { _,_ -> },
    viewModel: ActivitiesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.loading){
        true -> Loading(user)
        false -> List(
            state = state,
            user = user,
            navigate = navigate,
            onOpen = onOpen,
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun Frame(
    user: User,
    navigate: (AppRoute) -> Unit = {},
    enableMenu: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scope = rememberCoroutineScope()

    NavigationDrawer(
        navigate = navigate,
        currentRoute = AppRoute.Activities,
    ) { drawerState ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Activities", modifier = Modifier.padding(start = 20.dp).testTag("title")) },
                    navigationIcon = {
                        Button(
                            modifier = Modifier.testTag("menu"),
                            icon = Icons.Filled.Menu,
                            enabled = enableMenu,
                            onClick = { scope.launch { drawerState.open() } },
                        )
                    },
                    actions = {
                        AsyncImage(
                            model = user.profileImageUrl.takeIf { it.isNotEmpty() } ?: Icons.Default.Person,
                            contentDescription = user.name,
                            modifier = Modifier
                                .padding(end = 14.dp)
                                .size(46.dp)
                                .clip(CircleShape)
                        )
                    }
                )
            },
            content = content
        )
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun List(
    @PreviewParameter(ActivitiesPreview::class) state: ActivitiesState,
    user: User = User.ANONYM,
    navigate: (AppRoute) -> Unit = {},
    onOpen: (Long, Long?) -> Unit = { _,_ -> },
    onAction: (ActivitiesAction) -> Unit = {},
) {
    Frame(
        user = user,
        navigate = navigate,
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = padding.add(horizontal = 20.dp),
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .testTag("activity_list")
        ) {
            when(state.activities.isEmpty()){
                true -> item { Text("No activities", modifier = Modifier.testTag("no_activities")) }
                false -> items(state.activities, key = { it.id }) { activity ->
                    ActivityCard(
                        activity = activity,
                        modifier = Modifier.fillMaxWidth().testTag("activity_${activity.id}"),
                        onClick = { onOpen(activity.id, activity.stravaId) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
private fun Loading(
    user: User = User.ANONYM,
) {
    Frame(
        user = user,
        enableMenu = false,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(horizontal = 20.dp))
                .testTag("activities_loading")
        ) {
            CircularProgressIndicator(
                strokeWidth = 8.dp,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}