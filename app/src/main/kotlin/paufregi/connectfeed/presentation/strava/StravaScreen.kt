package paufregi.connectfeed.presentation.strava

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.components.StatusInfo
import paufregi.connectfeed.presentation.ui.components.StatusInfoType

@Preview
@Composable
@ExperimentalMaterial3Api
internal fun StravaScreen(
    @PreviewParameter(StravaStatePreview::class) state: StravaState,
    onComplete: () -> Unit = {},
) {
    SimpleScaffold {
        when (state) {
            is StravaState.Processing -> Loading(it)
            is StravaState.Success -> StatusInfo(
                type = StatusInfoType.Success,
                text = "Strava linked",
                actionButton = { Button(text = "Ok", onClick = onComplete) },
                paddingValues = it
            )

            is StravaState.Failure -> StatusInfo(
                type = StatusInfoType.Failure,
                text = "Link failed",
                actionButton = { Button(text = "Ok", onClick = onComplete) },
                paddingValues = it
            )
        }
    }
}