package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.data.api.garmin.serializers.ActivityTypeSerializer

@Serializable(with = ActivityTypeSerializer::class)
data class ActivityTypeDto(
    val id: Long,
    val key: String,
    val type: ActivityType
)