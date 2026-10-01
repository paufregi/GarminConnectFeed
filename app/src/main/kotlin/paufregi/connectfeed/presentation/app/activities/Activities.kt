package paufregi.connectfeed.presentation.app.activities

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.app.Route
import paufregi.connectfeed.presentation.app.activities.edit.ActivityScreen
import paufregi.connectfeed.presentation.app.activities.list.ActivitiesScreen
import paufregi.connectfeed.presentation.app.profiles.edit.ProfileScreen
import paufregi.connectfeed.presentation.app.profiles.list.ProfilesScreen

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
fun Activities(
    padding: PaddingValues = PaddingValues(),
) {
    val backStack = rememberNavBackStack(Route.ActivityList)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.ActivityList> {
                ActivitiesScreen(
                    onOpen = { id, stravaId -> backStack.add(Route.ActivityEdit(id, stravaId)) },
                    padding = padding
                )
            }

            entry<Route.ActivityEdit> { route ->
                ActivityScreen(
                    id = route.id,
                    stravaId = route.stravaId,
                    onBack = { backStack.removeLastOrNull() },
                    padding = padding,
                )
            }
        }
    )
}