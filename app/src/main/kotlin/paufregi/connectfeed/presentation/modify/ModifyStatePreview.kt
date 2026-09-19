package paufregi.connectfeed.presentation.modify

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState
import kotlin.time.Instant

class ModifyStatePreview : PreviewParameterProvider<ModifyState> {
    private val activity = Activity(
        id = 1,
        name = "Morning run",
        type = ActivityType.Running,
        distance = 12340.0,
        date = Instant.fromEpochMilliseconds(1735693200000),
    )

    private val stravaActivity = Activity(
        id = 2,
        name = "Morning run",
        type = ActivityType.Running,
        distance = 12350.0,
        date = Instant.fromEpochMilliseconds(1735693200000),
    )

    private val course = Course(
        id = 1,
        name = "River trail",
        type = ActivityType.Running,
        distance = 12500.0,
    )

    private val profile = Profile(
        id = 1,
        name = "Run profile",
        type = ActivityType.Running,
        water = 10,
        customWater = true,
        feelAndEffort = true,
        course = course,
    )

    override val values = sequenceOf(
        ModifyState(
            process = ProcessState.Idle,
            activities = listOf(activity),
        ),
        ModifyState(
            process = ProcessState.Idle,
            activities = listOf(activity),
            stravaActivities = listOf(stravaActivity),
            eventTypes = listOf(EventType.Training),
            courses = listOf(course),
            activity = activity,
            stravaActivity = stravaActivity,
            name = "Updated run",
            eventType = EventType.Training,
            course = course,
            description = "Description",
            water = 10,
            effort = 80f,
            feel = 50f,
            trainingEffect = true,
        ),
        ModifyState(
            process = ProcessState.Idle,
            activities = listOf(activity),
            stravaActivities = listOf(stravaActivity),
            profiles = listOf(profile),
            activity = activity,
            stravaActivity = stravaActivity,
            mode = ModifyMode.Profile,
            profile = profile,
            description = "Description",
            water = 10,
            effort = 50f,
            feel = 50f,
        ),
    )
}
