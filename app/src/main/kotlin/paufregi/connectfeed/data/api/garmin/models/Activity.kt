package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.utils.serializers.InstantSerializer
import kotlin.time.Instant

@Serializable
data class Activity(
    @SerialName("activityId")
    val id: Long,
    @SerialName("activityName")
    val name: String,
    @SerialName("activityType")
    val type: ActivityType,
    @SerialName("eventType")
    val eventType: EventType,
    @SerialName("distance")
    val distance: Double,
    @SerialName("trainingEffectLabel")
    val trainingEffectLabel: String?,
    @SerialName("beginTimestamp")
    @Serializable(with = InstantSerializer::class)
    val beginTimestamp: Instant,
    @SerialName("workoutId")
    val workoutId: Long?,
)