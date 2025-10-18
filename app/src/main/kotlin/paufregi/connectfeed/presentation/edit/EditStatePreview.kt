package paufregi.connectfeed.presentation.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.presentation.ui.models.ProcessState

class EditStatePreview : PreviewParameterProvider<EditState> {
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

    val course = Course(
        id = 1,
        name = "course",
        type = ActivityType.Running,
        distance = 1250.0
    )

    override val values = sequenceOf(
        EditState(
            process = ProcessState.Idle,
            activities = listOf(activity),
            activity = activity,
            name = "new activity",
            eventType = EventType.Training,
            course = course,
            water = 10,
            effort = 80f,
            feel = 50f,
        ),
        EditState(
            process = ProcessState.Idle,
            activities = listOf(activity),
            stravaActivities = listOf(stravaActivity),
            activity = activity,
            stravaActivity = stravaActivity,
            name = "new activity",
            eventType = EventType.Training,
            course = course,
            description = "description",
            water = 10,
            effort = 80f,
            feel = 50f,
            trainingEffect = true,
        ),
    )
}
