package paufregi.connectfeed.presentation.app.profiles

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.app.ProfilesRoute
import paufregi.connectfeed.presentation.app.profiles.edit.ProfileScreen
import paufregi.connectfeed.presentation.app.profiles.list.ProfilesScreen

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
fun Profiles(
    user: User,
    navigate: (AppRoute) -> Unit
) {
    val profileStack = rememberNavBackStack(ProfilesRoute.List)

    NavDisplay(
        backStack = profileStack,
        onBack = { profileStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<ProfilesRoute.List> { ProfilesScreen(
                user = user,
                navigate = navigate,
                onOpen = { id -> profileStack.add(ProfilesRoute.Edit(id)) },
                onCreate = { profileStack.add(ProfilesRoute.Edit()) },
            ) }
            entry<ProfilesRoute.Edit> { route -> ProfileScreen(
                id = route.id,
                onDone = { profileStack.removeLastOrNull() },
            ) }
        }
    )
}