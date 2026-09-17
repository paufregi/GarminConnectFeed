package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.garmin.serializers.ActivityTypeSerializer
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

@Serializable(with = ActivityTypeSerializer::class)
data class ActivityType(
    val id: Long,
    val key: String,
    val type: CoreActivityType
)