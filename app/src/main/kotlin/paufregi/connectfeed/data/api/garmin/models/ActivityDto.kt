package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityDto(
    @SerialName("activityId")
    val id: Long,
    @SerialName("activityName")
    val name: String,
    @SerialName("activityType")
    val type: ActivityTypeDto,
    @SerialName("eventType")
    val eventType: EventTypeDto?,
    @SerialName("distance")
    val distance: Double,
    @SerialName("trainingEffectLabel")
    val trainingEffectLabel: String?,
    @SerialName("beginTimestamp")
    val beginTimestamp: Long?,
    @SerialName("workoutId")
    val workoutId: Long?,
)