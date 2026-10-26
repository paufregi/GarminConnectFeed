package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateActivityDto(
    @SerialName("activityId")
    val id: Long,
    @SerialName("activityName")
    val name: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("eventTypeDTO")
    val eventType: EventTypeDto,
    @SerialName("metadataDTO")
    val metadata: MetadataDto?,
    @SerialName("summaryDTO")
    val summary: SummaryDto?
)