package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.EventType
import javax.inject.Inject

class GetEventTypes @Inject constructor() {
    operator fun invoke(): List<EventType> = listOf(
        EventType.Race,
        EventType.Recreation,
        EventType.SpecialEvent,
        EventType.Training,
        EventType.Transportation,
        EventType.Touring,
        EventType.Geocaching,
        EventType.Fitness,
        EventType.Uncategorized,
    ).sortedBy { it.order }
}