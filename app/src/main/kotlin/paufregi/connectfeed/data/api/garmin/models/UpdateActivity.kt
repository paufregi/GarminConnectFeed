package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.api.garmin.serializers.EventTypeSerializer

@Serializable
data class UpdateActivity(
    @SerialName("activityId")
    val id: Long,
    @SerialName("activityName")
    val name: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("eventTypeDTO")
    @Serializable(with = EventTypeSerializer::class)
    val eventType: EventType?,
    @SerialName("metadataDTO")
    val metadata: Metadata?,
    @SerialName("summaryDTO")
    val summary: Summary?
)