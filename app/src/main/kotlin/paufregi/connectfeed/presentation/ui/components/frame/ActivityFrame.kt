package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import paufregi.connectfeed.core.models.User

/** Frame for standalone activities (login, Strava link, weight sync) without navigation. */
@Composable
@ExperimentalMaterial3Api
fun ActivityFrame(
    title: String,
    user: User? = null,
    loading: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) = Frame(
    title = title,
    loading = loading,
    actions = { UserAvatar(user) },
    bottomBar = bottomBar,
    content = content,
)
