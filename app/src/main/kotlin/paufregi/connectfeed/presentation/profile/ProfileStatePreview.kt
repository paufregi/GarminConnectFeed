package paufregi.connectfeed.presentation.profile

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

class ProfileStatePreview : PreviewParameterProvider<ProfileState> {

    override val values = sequenceOf(
        ProfileState(
            process = ProcessState.Idle,
            profile = Profile(
                name = "Profile running",
                type = ActivityType.Running,
                eventType = EventType.Training,
                course = Course(1, "Course 1", 10.0, ActivityType.Running),
                water = 200,
                rename = true,
                customWater = true,
                feelAndEffort = true,
                trainingEffect = true
            ),
        ),
        ProfileState(
            process = ProcessState.Idle,
            profile = Profile(
                name = "Profile any",
                type = ActivityType.Any,
                eventType = null,
                course = null,
                water = 200,
                rename = false,
            ),
        ),
        ProfileState(
            process = ProcessState.Idle,
            profile = Profile(
                name = "Profile strength",
                type = ActivityType.StrengthTraining,
                eventType = null,
                course = null,
                water = 200,
                rename = false,
            ),
        ),

        )
}