package paufregi.connectfeed.presentation.quickedit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.ProfileType
import paufregi.connectfeed.presentation.ui.models.ProcessState

class QuickEditStatePreview : PreviewParameterProvider<QuickEditState> {
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

    val profile = Profile(
        id = 1,
        name = "profile",
        type = ProfileType.Running,
        course = Course(
            id = 1,
            name = "course",
            type = ActivityType.Running,
            distance = 1250.0
        )
    )

    val fullProfile = Profile(
        id = 1,
        name = "profile",
        type = ProfileType.Running,
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

    override val values = sequenceOf(
        QuickEditState(
            process = ProcessState.Idle,
            activities = listOf(activity),
            profiles = listOf(profile),
            activity = activity,
            profile = profile
        ),
        QuickEditState(
            process = ProcessState.Idle,
            activities = listOf(activity),
            profiles = listOf(fullProfile),
            activity = activity,
            profile = fullProfile,
            feel = 50f,
            effort = 50f,
        ),
        QuickEditState(
            process = ProcessState.Idle,
            activities = listOf(activity),
            stravaActivities = listOf(stravaActivity),
            profiles = listOf(fullProfile),
            activity = activity,
            stravaActivity = stravaActivity,
            profile = fullProfile,
            water = 20,
            feel = 50f,
            effort = 50f,
            description = "description",
        )
    )
}
