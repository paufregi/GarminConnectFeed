package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Athlete(
    @SerialName("id")
    val id: Long,
    @SerialName("bikes")
    val bikes: List<Bike> = emptyList(),
    @SerialName("shoes")
    val shoes: List<Shoe> = emptyList(),
)
