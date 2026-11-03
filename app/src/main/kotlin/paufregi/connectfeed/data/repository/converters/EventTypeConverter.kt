package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.api.garmin.models.EventTypeDto

fun EventTypeDto.toCore(): EventType? = when (this.id) {
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