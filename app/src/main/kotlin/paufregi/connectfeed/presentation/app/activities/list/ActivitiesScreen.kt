package paufregi.connectfeed.presentation.app.activities.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import paufregi.connectfeed.presentation.activities.ActivitiesAction
import paufregi.connectfeed.presentation.ui.components.ActivityCard
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun ActivitiesScreen(
    padding: PaddingValues = PaddingValues(),
    viewModel: ActivityListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.loading) {
        true -> Loading()
        false -> ActivityList(state, viewModel::onAction, padding)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun ActivityList(
    @PreviewParameter(ActivitiesPreview::class) state: ActivitiesState,
    onAction: (ActivitiesAction) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().testTag("activity_list_content")
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
            if (state.activities.isEmpty()) {
                item { Text("No activities", modifier = Modifier.testTag("no_activities")) }
            } else {
                items(state.activities, key = { it.id }) { activity ->
                    ActivityCard(
                        activity = activity,
                        modifier = Modifier.fillMaxWidth().testTag("profile_${activity.id}"),
                        onClick = { },
                    )
                }
            }
        }
    }
}

