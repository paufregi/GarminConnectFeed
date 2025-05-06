package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class UpdateActivity(
    @SerialName("activityId")
    val id: Long,
    @SerialName("activityName")
    val name: String?,
    @SerialName("eventTypeDTO")
    val eventType: EventType,
    @SerialName("metadataDTO")
    val metadata: Metadata?,
    @SerialName("summaryDTO")
    val summary: Summary?
)