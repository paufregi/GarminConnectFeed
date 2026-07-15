package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Athlete(
    @SerialName("bikes")
    val bikes: List<Gear>,

    @SerialName("shoes")
    val shoes: List<Gear>,
)
