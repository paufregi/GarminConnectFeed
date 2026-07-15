package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gear(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)
