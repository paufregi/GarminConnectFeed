package paufregi.connectfeed.presentation.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.util.fastForEachIndexed
import paufregi.connectfeed.presentation.Navigator
import androidx.compose.material3.NavigationBar as MaterialNavigationBar
import androidx.compose.material3.NavigationBarItem as MaterialNavigationBarItem

@Composable
fun NavigationBar(
    items: List<NavigationItem>,
    selectedIndex: Int = 0,
    nav: Navigator = Navigator()
) {
    MaterialNavigationBar(
        modifier = Modifier.testTag("navigation_bar")
    ) {
        items.fastForEachIndexed { index, item ->
            MaterialNavigationBarItem(
                modifier = Modifier.testTag("nav_${item.label.lowercase()}"),
                selected = index == selectedIndex,
                onClick = { nav.navigateTopLevel(item.route) },
                label = { Text(item.label) },
                icon = { Icon(item.icon, item.label) }
            )
        }
    }
}
