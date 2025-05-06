package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import paufregi.connectfeed.core.models.EventType as CoreEventType

@Serializable
@JsonIgnoreUnknownKeys
data class EventType(
    @SerialName("typeId")
    val id: Long?,
    @SerialName("typeKey")
    val key: String?,
) {
    fun toCore(): CoreEventType? = when (this.id) {
        1L -> CoreEventType.Race
        2L -> CoreEventType.Recreation
        3L -> CoreEventType.SpecialEvent
        4L -> CoreEventType.Training
        5L -> CoreEventType.Transportation
        6L -> CoreEventType.Touring
        7L -> CoreEventType.Geocaching
        8L -> CoreEventType.Fitness
        9L -> CoreEventType.Uncategorized
        else -> CoreEventType.Uncategorized
    }
}