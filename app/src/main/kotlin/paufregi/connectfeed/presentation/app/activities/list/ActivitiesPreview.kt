package paufregi.connectfeed.presentation.app.activities.list

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import java.time.LocalDateTime

class ActivitiesPreview : PreviewParameterProvider<ActivitiesState> {
    override val values = sequenceOf(
        ActivitiesState(),
        ActivitiesState(
            activities = listOf(
                Activity(
                    id = 1,
                    name = "Morning ride",
                    type = ActivityType.Cycling,
                    eventType = EventType.Uncategorized,
                    date = LocalDateTime.of(2026, 8, 4, 16, 30),
                    distance = 15010.0,
                ),
                Activity(
                    id = 2,
                    stravaId = 1,
                    name = "Morning run",
                    type = ActivityType.Running,
                    eventType = EventType.Uncategorized,
                    date = LocalDateTime.of(2026, 8, 4, 16, 30),
                    distance = 5010.0,
                )
            )
        )
    )
}

