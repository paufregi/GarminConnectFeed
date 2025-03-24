package paufregi.connectfeed.presentation.quickedit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

class QuickEditStatePreview : PreviewParameterProvider<QuickEditState> {
    override val values = sequenceOf(
        QuickEditState(ProcessState.Processing),
        QuickEditState(ProcessState.Success("Activity updates")),
        QuickEditState(ProcessState.Failure("Failed to update activity")),
        QuickEditState(
            activity = Activity(
                id = 1,
                name = "activity",
                type = ActivityType.Running,
                distance = 12345.0
            ),
            profile = Profile(name = "name", activityType = ActivityType.Running)
        ),
        QuickEditState(
            activity = Activity(
                id = 2,
                name = "activity",
                type = ActivityType.Cycling,
                distance = 12345.0
            ),
            profile = Profile(name = "name", activityType = ActivityType.Cycling),
            feel = 50f,
            effort = 50f,
        ),
        QuickEditState(
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
            profile = Profile(
                name = "name",
                activityType = ActivityType.Cycling,
                water = 1,
                customWater = true
            ),
            feel = 50f,
            effort = 50f,
        ),
    )
}
