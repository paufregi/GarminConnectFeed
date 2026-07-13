package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gear(
    @SerialName("uuid")
    val id: String,

    @SerialName("brand")
    val brand: String?,

    @SerialName("model")
    val model: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("gearType")
    val type: String?,
)