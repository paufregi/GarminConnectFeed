package paufregi.connectfeed.presentation.ui.components.loading

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.components.frame.ActivityFrame

@Composable
@ExperimentalMaterial3Api
fun ActivityLoading(
    title: String,
    user: User? = null
) {
    ActivityFrame(
        title = title,
        testTag = "activity_loading",
        user = user,
    ) { Loading(it) }
}