package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.data.api.garmin.serializers.ActivityTypeSerializer

@Serializable(with = ActivityTypeSerializer::class)
data class ActivityTypeDto(
    @SerialName("typeId")
    val id: Long,
    @SerialName("typeKey")
    val key: String,
    val type: ActivityType
)