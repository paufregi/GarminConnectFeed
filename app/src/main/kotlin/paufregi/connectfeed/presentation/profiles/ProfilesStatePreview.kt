package paufregi.connectfeed.presentation.profiles

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.ProfileType

class ProfilesStatePreview : PreviewParameterProvider<ProfilesState> {
    override val values = sequenceOf(
        ProfilesState(profiles = emptyList()),
        ProfilesState(
            profiles = listOf(
                Profile(
                    name = "Commute to home",
                    rename = true,
                    eventType = EventType.Transportation,
                    type = ProfileType.Cycling,
                    course = Course(1, "Commute to home", 10.0, ActivityType.Cycling),
                    water = 550
                ),
                Profile(
                    name = "Ponsonby/Westhaven",
                    rename = true,
                    eventType = EventType.Transportation,
                    type = ProfileType.Running,
                    course = Course(2, "Ponsonby/Westhaven", 10.0, ActivityType.Running)
                ),
                Profile(
                    name = "Gym",
                    rename = true,
                    type = ProfileType.Strength,
                    water = 100
                ),
                Profile(
                    name = "Just water",
                    rename = true,
                    type = ProfileType.Any,
                    water = 100
                )
            )
        )
    )
}