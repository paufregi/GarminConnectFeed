package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.api.garmin.serializers.EventTypeSerializer
import paufregi.connectfeed.data.api.garmin.serializers.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class Activity(
    @SerialName("activityId")
    val id: Long,
    @SerialName("activityName")
    val name: String,
    @SerialName("activityType")
    val type: ActivityType,
    @SerialName("eventType")
    @Serializable(with = EventTypeSerializer::class)
    val eventType: EventType,
    @SerialName("distance")
    val distance: Double,
    @SerialName("trainingEffectLabel")
    val trainingEffectLabel: String?,
    @SerialName("startTimeLocal")
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDateTime: LocalDateTime,
    @SerialName("workoutId")
    val workoutId: Long?,
)