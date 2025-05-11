package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.garmin.converters.TrainingEffectConverter
import java.time.Instant
import kotlin.math.round
import paufregi.connectfeed.core.models.Activity as CoreActivity

@Serializable
data class Activity(
    @SerialName("activityId")
    val id: Long,
    @SerialName("activityName")
    val name: String,
    @SerialName("activityType")
    val type: ActivityType,
    @SerialName("eventType")
    val eventType: EventType?,
    @SerialName("distance")
    val distance: Double,
    @SerialName("trainingEffectLabel")
    val trainingEffectLabel: String?,
    @SerialName("beginTimestamp")
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