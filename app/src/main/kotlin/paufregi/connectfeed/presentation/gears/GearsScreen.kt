package paufregi.connectfeed.presentation.gears

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
import androidx.compose.material.icons.filled.Sync
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
import paufregi.connectfeed.presentation.ui.components.NavigationScaffold
import paufregi.connectfeed.presentation.ui.utils.iconFor

@Composable
@ExperimentalMaterial3Api
internal fun GearsScreen(
    nav: NavHostController = rememberNavController(),
) {
    val viewModel = hiltViewModel<GearsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationScaffold(
        topItems = Navigation.topItems,
        bottomItems = Navigation.bottomItems,
        selectedIndex = Navigation.GEARS,
        nav = nav,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { nav.navigate(Route.Profile()) },
                modifier = Modifier.testTag("create_profile")
            ) { Icon(Icons.Default.Sync, "Synch") }
        }
    ) {
        GearsContent(state, viewModel::onAction, nav, it)
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun GearsContent(
    @PreviewParameter(GearsStatePreview::class) state: GearsState,
    onAction: (GearsAction) -> Unit = {},
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
            .testTag("gears_content")
    ) {
        if (state.gears.isEmpty()) {
            item { Text("No gears") }
        } else {
            item { Text("") }
            itemsIndexed(state.gears) { _, it ->
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
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
