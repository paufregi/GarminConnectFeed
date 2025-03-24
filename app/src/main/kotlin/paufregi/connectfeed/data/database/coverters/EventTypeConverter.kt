package paufregi.connectfeed.data.database.coverters

import androidx.room.TypeConverter
import paufregi.connectfeed.core.models.EventType

class EventTypeConverter {
    @TypeConverter
    fun fromId(type: Long?): EventType? = when (type) {
        EventType.Race.id -> EventType.Race
        EventType.Recreation.id -> EventType.Recreation
        EventType.SpecialEvent.id -> EventType.SpecialEvent
        EventType.Training.id -> EventType.Training
        EventType.Transportation.id -> EventType.Transportation
        EventType.Touring.id -> EventType.Touring
        EventType.Geocaching.id -> EventType.Geocaching
        EventType.Fitness.id -> EventType.Fitness
        EventType.Uncategorized.id -> EventType.Uncategorized
        else -> null
    }

    @TypeConverter
    fun toId(type: EventType?): Long? = type?.id
}