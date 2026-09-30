package paufregi.connectfeed.presentation.app.profiles.list

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile

class ProfilesStatePreview : PreviewParameterProvider<ProfilesState> {
    override val values = sequenceOf(
        ProfilesState(),
        ProfilesState(
            listOf(
                Profile(id = 1, name = "Road bike", type = ActivityType.Cycling, eventType = EventType.Training, gear = true),
                Profile(id = 2, name = "Long run", type = ActivityType.Running, eventType = EventType.Race, water = 500),
            )
        ),
    )
}

