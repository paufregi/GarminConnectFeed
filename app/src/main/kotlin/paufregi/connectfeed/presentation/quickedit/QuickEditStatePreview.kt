package paufregi.connectfeed.presentation.quickedit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

class QuickEditStatePreview : PreviewParameterProvider<QuickEditState> {
    override val values = sequenceOf(
        QuickEditState(ProcessState.Processing),
        QuickEditState(ProcessState.Success("Activity updates")),
        QuickEditState(ProcessState.Failure("Failed to update activity")),
        QuickEditState(
            process = ProcessState.Idle,
            activities = listOf(
                Activity(
                    id = 1,
                    name = "activity",
                    type = ActivityType.Running,
                    distance = 1230.0,
                )
            ),
            profiles = listOf(
                Profile(
                    id = 1,
                    name = "profile",
                    activityType = ActivityType.Running,
                    course = Course(
                        id = 1,
                        name = "course",
                        type = ActivityType.Running,
                        distance = 1250.0
                    )
                )
            ),
            activity = Activity(
                id = 1,
                name = "activity",
                type = ActivityType.Running,
                distance = 1230.0,
            ),
            profile = Profile(
                id = 1,
                name = "profile",
                activityType = ActivityType.Running,
                course = Course(
                    id = 1,
                    name = "course",
                    type = ActivityType.Running,
                    distance = 1250.0
                )
            )
        ),
        QuickEditState(
            process = ProcessState.Idle,
            activities = listOf(
                Activity(
                    id = 1,
                    name = "activity",
                    type = ActivityType.Running,
                    distance = 1230.0,
                )
            ),
            profiles = listOf(
                Profile(
                    id = 1,
                    name = "profile",
                    activityType = ActivityType.Running,
                    water = 10,
                    customWater = true,
                    feelAndEffort = true,
                    course = Course(
                        id = 1,
                        name = "course",
                        type = ActivityType.Running,
                        distance = 1250.0
                    )
                )
            ),
            activity = Activity(
                id = 1,
                name = "activity",
                type = ActivityType.Running,
                distance = 1230.0,
            ),
            profile = Profile(
                id = 1,
                name = "profile",
                activityType = ActivityType.Running,
                water = 10,
                customWater = true,
                feelAndEffort = true,
                course = Course(
                    id = 1,
                    name = "course",
                    type = ActivityType.Running,
                    distance = 1250.0
                )
            ),
            feel = 50f,
            effort = 50f,
        ),
        QuickEditState(
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
                    id = 2,
                    name = "strava activity",
                    type = ActivityType.Running,
                    distance = 1240.0,
                )
            ),
            profiles = listOf(
                Profile(
                    id = 1,
                    name = "profile",
                    activityType = ActivityType.Running,
                    water = 10,
                    customWater = true,
                    feelAndEffort = true,
                    course = Course(
                        id = 1,
                        name = "course",
                        type = ActivityType.Running,
                        distance = 1250.0
                    )
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
            profile = Profile(
                id = 1,
                name = "profile",
                activityType = ActivityType.Running,
                water = 10,
                customWater = true,
                feelAndEffort = true,
                course = Course(
                    id = 1,
                    name = "course",
                    type = ActivityType.Running,
                    distance = 1250.0
                )
            ),
            water = 20,
            feel = 50f,
            effort = 50f,
            description = "description",
        )
    )
}
