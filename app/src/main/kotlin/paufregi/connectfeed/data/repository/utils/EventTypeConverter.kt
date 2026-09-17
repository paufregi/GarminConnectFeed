package paufregi.connectfeed.data.repository.utils

import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.api.garmin.models.EventType as GarminEventType

object EventTypeConverter {

    fun garmin(type: GarminEventType): EventType = when (type.id) {
        1L -> EventType.Race
        2L -> EventType.Recreation
        3L -> EventType.SpecialEvent
        4L -> EventType.Training
        5L -> EventType.Transportation
        6L -> EventType.Touring
        7L -> EventType.Geocaching
        8L -> EventType.Fitness
        9L -> EventType.Uncategorized
        else -> EventType.Uncategorized
    }
}