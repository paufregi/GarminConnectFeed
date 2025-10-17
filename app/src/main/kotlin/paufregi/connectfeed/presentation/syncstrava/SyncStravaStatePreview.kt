package paufregi.connectfeed.presentation.syncstrava

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.ui.models.ProcessState

class SyncStravaStatePreview : PreviewParameterProvider<SyncStravaState> {

    val activity = Activity(
        id = 1,
        name = "activity",
        type = ActivityType.Running,
        distance = 1230.0,
    )

    val stravaActivity = Activity(
        id = 2,
        name = "strava activity",
        type = ActivityType.Running,
        distance = 1240.0,
    )

    override val values = sequenceOf(
        SyncStravaState(
            process = ProcessState.Idle,
            activities = listOf(activity),
            stravaActivities = listOf(stravaActivity),
            activity = activity,
            stravaActivity = stravaActivity,
            description = "description",
            trainingEffect = true,
        ),
    )
}
