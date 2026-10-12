package paufregi.connectfeed.presentation.app.activities.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import paufregi.connectfeed.presentation.ui.components.cards.ActivityCard
import paufregi.connectfeed.presentation.ui.components.frame.AppFrame
import paufregi.connectfeed.presentation.ui.components.loading.AppLoading
import paufregi.connectfeed.presentation.ui.utils.add

private const val TITLE = "Activities"

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
        true -> AppLoading(TITLE, AppRoute.Activities)
        false -> List(
            state = state,
            user = user,
            navigate = navigate,
            onOpen = onOpen,
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
    AppFrame(
        title = TITLE,
        user = user,
        currentRoute = AppRoute.Activities,
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
