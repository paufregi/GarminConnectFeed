package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.Gear

@Serializable
data class Athlete(
    @SerialName("id")
    val id: Long,
    @SerialName("bikes")
    val bikes: List<Bike> = emptyList(),
    @SerialName("shoes")
    val shoes: List<Shoe> = emptyList(),
)

fun Athlete.toGearList(): List<Gear> = buildList {
    addAll(shoes.map { it.toGear() })
    addAll(bikes.map { it.toGear() })
}
