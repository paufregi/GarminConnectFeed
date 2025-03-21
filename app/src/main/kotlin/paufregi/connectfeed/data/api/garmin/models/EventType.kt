package paufregi.connectfeed.data.api.garmin.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.EventType as CoreEventType

@Keep
@Serializable
data class EventType(
    @SerializedName("typeId")
    val id: Long?,
    @SerializedName("typeKey")
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