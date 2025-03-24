package paufregi.connectfeed.data.api.garmin.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.garmin.converters.TrainingEffectConverter
import java.time.Instant
import kotlin.math.round
import paufregi.connectfeed.core.models.Activity as CoreActivity

@Keep
@Serializable
data class Activity(
    @SerializedName("activityId")
    val id: Long,
    @SerializedName("activityName")
    val name: String,
    @SerializedName("activityType")
    val type: ActivityType,
    @SerializedName("eventType")
    val eventType: EventType?,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("trainingEffectLabel")
    val trainingEffectLabel: String?,
    @SerializedName("beginTimestamp")
    val beginTimestamp: Long?,
) {
    fun toCore(): CoreActivity =
        CoreActivity(
            id = this.id,
            name = this.name,
            type = this.type.toCore(),
            eventType = this.eventType?.toCore(),
            distance = round(this.distance),
            trainingEffect = TrainingEffectConverter.convert(this.trainingEffectLabel),
            date = this.beginTimestamp?.let { Instant.ofEpochMilli(it) }
        )
}