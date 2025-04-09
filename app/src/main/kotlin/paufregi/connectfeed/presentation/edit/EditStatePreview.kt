package paufregi.connectfeed.presentation.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.presentation.ui.models.ProcessState

class EditStatePreview : PreviewParameterProvider<EditState> {
    override val values = sequenceOf(
        EditState(ProcessState.Processing),
        EditState(ProcessState.Success("Activity updates")),
        EditState(ProcessState.Failure("Failed to update activity")),
        EditState(
            process = ProcessState.Idle,
            activities = listOf(
                Activity(
                    id = 1,
                    name = "activity",
                    type = ActivityType.Running,
                    distance = 1230.0,
                )
            ),
            activity = Activity(
                id = 1,
                name = "activity",
                type = ActivityType.Running,
                distance = 1230.0,
            ),
            name = "new activity",
            eventType = EventType.Training,
            course = Course(
                id = 1,
                name = "course",
                type = ActivityType.Running,
                distance = 1250.0
            ),
            water = 10,
            effort = 80f,
            feel = 50f,
        ),
        EditState(
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
            name = "new activity",
            eventType = EventType.Training,
            course = Course(
                id = 1,
                name = "course",
                type = ActivityType.Running,
                distance = 1250.0
            ),
            description = "description",
            water = 10,
            effort = 80f,
            feel = 50f,
            trainingEffect = true,
        ),
    )
}
