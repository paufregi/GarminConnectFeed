package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gear(
    @SerialName("uuid")
    val id: String,
    @SerialName("gearType")
    val type: String,
    val brand: String,
    val model: String?,
    val name: String?
)