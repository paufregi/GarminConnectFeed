package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Gear as CoreGear

@Serializable
data class Gear(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
) {
    fun toCore(type: GearType): CoreGear =
        CoreGear(
            id = id,
            name = name,
            type = type
        )

}
