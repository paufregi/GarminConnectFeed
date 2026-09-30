package paufregi.connectfeed.presentation.app.profiles

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
import paufregi.connectfeed.presentation.app.profiles.edit.ProfileScreen
import paufregi.connectfeed.presentation.app.profiles.list.ProfilesScreen

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
fun Profiles(
    padding: PaddingValues = PaddingValues(),
) {
    val profileStack = rememberNavBackStack(Route.ProfileList)

    NavDisplay(
        backStack = profileStack,
        onBack = { profileStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.ProfileList> { ProfilesScreen(
                onOpen = { id -> profileStack.add(Route.ProfileEdit(id)) },
                onCreate = { profileStack.add(Route.ProfileEdit()) },
                padding = padding,
            ) }
            entry<Route.ProfileEdit> { route -> ProfileScreen(
                id = route.id,
                onDone = { profileStack.removeLastOrNull() },
                padding = padding,
            ) }
        }
    )
}