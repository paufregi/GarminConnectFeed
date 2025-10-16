package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import paufregi.connectfeed.presentation.HomeNavigation
import paufregi.connectfeed.presentation.Navigation
import paufregi.connectfeed.presentation.ui.models.ProcessState

@Composable
@ExperimentalMaterial3Api
fun Screen(
    tagName: String = "screen",
    state: ProcessState,
    processing : @Composable (PaddingValues) -> Unit = { Loading(it) },
    success : @Composable (ProcessState.Success, PaddingValues) -> Unit,
    failure : @Composable (ProcessState.Failure, PaddingValues) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    SimpleScaffold {
        when (state) {
            is ProcessState.Processing -> processing(it)
            is ProcessState.Success -> success(state, it)
            is ProcessState.Failure -> failure(state, it)
            is ProcessState.Idle -> Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 20.dp)
                    .testTag(tagName)
            ) { content(it) }
        }
    }
}


@Composable
@ExperimentalMaterial3Api
fun Screen(
    tagName: String = "screen",
    navigationIndex: Int = 0,
    nav: NavHostController = rememberNavController(),
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    state: ProcessState,
    processing: @Composable (PaddingValues) -> Unit = { Loading(it) },
    success: @Composable (ProcessState.Success, PaddingValues) -> Unit,
    failure: @Composable (ProcessState.Failure, PaddingValues) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    when (state) {
        is ProcessState.Processing -> SimpleScaffold { processing(it) }
        is ProcessState.Success -> SimpleScaffold { success(state, it) }
        is ProcessState.Failure-> SimpleScaffold { failure(state, it) }
        is ProcessState.Idle -> NavigationScaffold(
            topItems = Navigation.topItems,
            bottomItems = Navigation.bottomItems,
            selectedIndex = navigationIndex,
            nav = nav,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .testTag(tagName)
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding(),
                        start = it.calculateLeftPadding(LayoutDirection.Ltr) + 20.dp,
                        end = it.calculateRightPadding(LayoutDirection.Ltr) + 20.dp,
                    ),
            ) {
                content(it)
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun Screen(
    tagName: String = "screen",
    hasStrava: Boolean,
    location: HomeNavigation,
    nav: NavHostController = rememberNavController(),
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    state: ProcessState,
    processing: @Composable (PaddingValues) -> Unit = { Loading(it) },
    success: @Composable (ProcessState.Success, PaddingValues) -> Unit,
    failure: @Composable (ProcessState.Failure, PaddingValues) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    when (state) {
        is ProcessState.Processing -> SimpleScaffold { processing(it) }
        is ProcessState.Success -> SimpleScaffold { success(state, it) }
        is ProcessState.Failure-> SimpleScaffold { failure(state, it) }
        is ProcessState.Idle -> NavigationScaffold(
            topItems = Navigation.topItems,
            bottomItems = Navigation.bottomItems,
            selectedIndex = location.menuIndex,
            bottomBar = @Composable {
                NavigationBar(
                    items = HomeNavigation.items(hasStrava),
                    selectedIndex = location.barIndex,
                    nav = nav,
                )
            },
            nav = nav,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .testTag(tagName)
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding(),
                        start = it.calculateLeftPadding(LayoutDirection.Ltr) + 20.dp,
                        end = it.calculateRightPadding(LayoutDirection.Ltr) + 20.dp,
                    ),
            ) {
                content(it)
            }
        }
    }
}