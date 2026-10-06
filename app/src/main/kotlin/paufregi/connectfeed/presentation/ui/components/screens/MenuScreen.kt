package paufregi.connectfeed.presentation.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.app.Navigation
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Loading

@Composable
@ExperimentalMaterial3Api
fun MenuScreen(
    user: User,
    isLoading: Boolean = false,
    currentRoute: AppRoute? = null,
    navigate: (AppRoute) -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val title = Navigation.menu.topItems.firstOrNull { it.route == currentRoute }?.label

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.75f)) {
                Text("Connect Feed", modifier = Modifier.padding(top = 24.dp, bottom = 32.dp, start = 4.dp))
                Navigation.menu.topItems.fastForEachIndexed { _, item ->
                    val isSelected = currentRoute == item.route
                    NavigationDrawerItem(
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        label = { Text(item.label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        selected = isSelected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (!isSelected) {
                                Log.i("MenuScreen", "onClick: ${item.route}")
                                navigate(item.route)
                            }
                        },
                        icon = { Icon(item.icon, item.label) },
                    )
                }
                Navigation.menu.bottomItems.let {
                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider()
                    it.fastForEachIndexed { _, item ->
                        val isSelected = currentRoute == item.route
                        NavigationDrawerItem(
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            label = { Text(item.label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            selected = isSelected,
                            onClick = {
                                scope.launch { drawerState.close() }
                                if (!isSelected) {
                                    Log.i("MenuScreen", "onClick: ${item.route}")
                                    navigate(item.route)
                                }
                            },
                            icon = { Icon(item.icon, item.label) },
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title ?: "Connect Feed", modifier = Modifier.padding(start = 20.dp).testTag("title")) },
                    navigationIcon = {
                        Button(
                            modifier = Modifier.testTag("menu"),
                            icon = Icons.Filled.Menu,
                            enabled = !isLoading,
                            onClick = { scope.launch { drawerState.open() } },
                        )
                    },
                    actions = {
                        title?.let {
                            AsyncImage(
                                model = user.profileImageUrl,
                                contentDescription = user.name,
                                modifier = Modifier
                                    .padding(end = 14.dp)
                                    .size(46.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                )
            },
            floatingActionButton = { if(!isLoading) floatingActionButton() },
            content = { padding ->
                when(isLoading) {
                    true -> Loading(padding)
                    false -> content(padding)
                }
            }
        )
    }
}
