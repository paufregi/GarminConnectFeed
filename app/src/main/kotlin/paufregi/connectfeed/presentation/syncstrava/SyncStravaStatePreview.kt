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
            process = ProcessState.Idle,
            activities = listOf(
                Activity(
                    id = 1,
                    name = "activity",
                    type = ActivityType.Running,
                    distance = 1230.0,
                )
            ),
            stravaActivities = listOf(
                Activity(
                    id = 1,
                    name = "strava activity",
                    type = ActivityType.Running,
                    distance = 1240.0,
                )
            ),
            activity = Activity(
                id = 1,
                name = "activity",
                type = ActivityType.Running,
                distance = 1230.0,
            ),
            stravaActivity = Activity(
                id = 2,
                name = "strava activity",
                type = ActivityType.Running,
                distance = 1240.0,
            ),
            description = "description",
            trainingEffect = true,
        ),
    )
}
