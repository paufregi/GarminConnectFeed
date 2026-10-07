package paufregi.connectfeed.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import kotlinx.coroutines.launch
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.app.Navigation

@Composable
fun NavigationDrawer(
    navigate: (AppRoute) -> Unit = {},
    currentRoute: AppRoute? = null,
    content: @Composable (DrawerState) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.75f)) {
                Text("Connect Feed", modifier = Modifier.padding(top = 24.dp, bottom = 32.dp, start = 4.dp))
                Navigation.menu.topItems.fastForEachIndexed { _, item ->
                    val isSelected = item.route == currentRoute
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
                        val isSelected = item.route == currentRoute
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
        },
    ) {
        content(drawerState)
    }
}