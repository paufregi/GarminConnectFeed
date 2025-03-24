package paufregi.connectfeed.presentation.syncstrava

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.ui.models.ProcessState

class SyncStravaStatePreview : PreviewParameterProvider<SyncStravaState> {
    override val values = sequenceOf(
        SyncStravaState(ProcessState.Processing),
        SyncStravaState(ProcessState.Success("Activity updates")),
        SyncStravaState(ProcessState.Failure("Failed to update activity")),
        SyncStravaState(
            activity = Activity(
                id = 2,
                name = "activity",
                type = ActivityType.Cycling,
                distance = 12345.0
            ),
            stravaActivity = Activity(
                id = 2,
                name = "activity",
                type = ActivityType.Cycling,
                distance = 12345.0
            ),
            description = "description",
            trainingEffect = true,
        ),
    )
}
