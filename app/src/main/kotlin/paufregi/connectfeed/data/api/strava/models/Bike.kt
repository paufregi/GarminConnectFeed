package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType

@Serializable
data class Bike(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String? = null,
    @SerialName("distance")
    val distance: Long = 0,
)

fun Bike.toGear() = Gear(
    id = id,
    name = name ?: "",
    type = GearType.Bike,
    distance = distance.toInt(),
)