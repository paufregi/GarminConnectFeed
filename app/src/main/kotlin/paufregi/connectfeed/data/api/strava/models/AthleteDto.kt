package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AthleteDto(
    @SerialName("id")
    val id: Long,
    @SerialName("bikes")
    val bikes: List<SummaryGearDto> = emptyList(),
    @SerialName("shoes")
    val shoes: List<SummaryGearDto> = emptyList(),
)