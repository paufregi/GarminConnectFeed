package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import paufregi.connectfeed.presentation.Route

data class NavigationItem(
    val index: Int,
    val label: String,
    val icon: ImageVector,
    val route: Route,
)

@Composable
@ExperimentalMaterial3Api
fun NavigationScaffold(
    topItems: List<NavigationItem> = emptyList(),
    bottomItems: List<NavigationItem> = emptyList(),
    selectedIndex: Int = 0,
    nav: NavHostController = rememberNavController(),
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                topItems.fastForEachIndexed { index, item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        label = { Text(item.label) },
                        selected = item.index == selectedIndex,
                        onClick = {
                            scope.launch { drawerState.close() }
                            nav.navigate(item.route)
                        },
                        icon = { Icon(item.icon, item.label) },
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider()
                bottomItems.fastForEachIndexed { index, item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        label = { Text(item.label) },
                        selected = item.index == selectedIndex,
                        onClick = {
                            scope.launch { drawerState.close() }
                            nav.navigate(item.route)
                        },
                        icon = { Icon(item.icon, item.label) },
                    )
                }
            }
        }
    ) {
        Scaffold(
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            bottomBar = bottomBar,
            topBar = {
                TopAppBar(
                    title = { Text("Connect Feed") },
                    actions = {
                        Button(
                            modifier = Modifier.testTag("menu"),
                            icon = Icons.Filled.Menu,
                            onClick = { scope.launch { drawerState.open() } }
                        )
                    }
                )
            },
            content = content
        )
    }
}
