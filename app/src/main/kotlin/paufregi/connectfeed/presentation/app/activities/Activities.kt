package paufregi.connectfeed.presentation.app.activities

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.ActivitiesRoute
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.app.activities.edit.EditScreen
import paufregi.connectfeed.presentation.app.activities.list.ActivitiesScreen
import paufregi.connectfeed.presentation.app.activities.quickedit.QuickEditScreen

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
fun Activities(
    user: User,
    navigate: (AppRoute) -> Unit = {},
) {
    val backStack = rememberNavBackStack(ActivitiesRoute.List)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<ActivitiesRoute.List> {
                ActivitiesScreen(
                    user = user,
                    navigate = navigate,
                    onOpen = { id, stravaId -> backStack.add(ActivitiesRoute.QuickEdit(id, stravaId)) },
                )
            }

            entry<ActivitiesRoute.Edit> { route ->
                EditScreen(
                    id = route.id,
                    stravaId = route.stravaId,
                    navBack = { backStack.removeLastOrNull() },
                    navQuickEdit = {
                        backStack.removeLastOrNull()
                        backStack.add(ActivitiesRoute.QuickEdit(route.id, route.stravaId))
                    }
                )
            }

            entry<ActivitiesRoute.QuickEdit> { route ->
                QuickEditScreen(
                    id = route.id,
                    stravaId = route.stravaId,
                    navBack = { backStack.removeLastOrNull() },
                    navEdit = {
                        backStack.removeLastOrNull()
                        backStack.add(ActivitiesRoute.Edit(route.id, route.stravaId))
                    }
                )
            }
        }
    )
}
