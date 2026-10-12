package paufregi.connectfeed.presentation.ui.components.loading

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.app.AppRoute
import paufregi.connectfeed.presentation.ui.components.frame.AppFrame

@Composable
@ExperimentalMaterial3Api
fun AppLoading(
    title: String,
    route: AppRoute,
    user: User? = null,
) {
    AppFrame(
        title = title,
        currentRoute = route,
        user = user,
        enableMenu = false,
    ) { Loading(it) }
}