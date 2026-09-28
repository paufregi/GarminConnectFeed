package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.ui.components.Button

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: Route,
)

data class MenuSpec(
    val topItems: List<NavigationItem>,
    val bottomItems: List<NavigationItem>,
)

@Composable
@ExperimentalMaterial3Api
fun Frame(
    enableMenu: Boolean = true,
    menuSpec: MenuSpec? = null,
    currentRoute: Route? = null,
    navigate: (Route) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val viewModel = hiltViewModel<FrameViewModel>()
    val user by viewModel.user.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Connected Feed")
                Spacer(modifier = Modifier.height(32.dp))
                menuSpec?.topItems?.fastForEachIndexed { _, item ->
                    val isSelected = currentRoute == item.route
                    NavigationDrawerItem(
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        label = { Text(item.label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        selected = isSelected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navigate(item.route)
                        },
                        icon = { Icon(item.icon, item.label) },
                    )
                }
                menuSpec?.bottomItems?.let {
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
                                navigate(item.route)
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
                    title = { Text("Connect Feed") },
                    navigationIcon = {
                        Button(
                            modifier = Modifier.testTag("menu"),
                            icon = Icons.Filled.Menu,
                            onClick = { scope.launch { drawerState.open() } },
                            enabled = enableMenu
                        )
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            AsyncImage(
                                model = user?.profileImageUrl,
                                contentDescription = user?.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                    }
                )
            },
            content = { padding -> content(padding)}
        )
    }
}

@Composable
@ExperimentalMaterial3Api
fun FrameLogin(
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Connect Feed") },
            )
        },
        content = { padding -> content(padding)}
    )
}