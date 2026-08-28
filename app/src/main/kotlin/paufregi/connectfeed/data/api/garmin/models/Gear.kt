package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.GearType
import kotlin.math.roundToInt
import paufregi.connectfeed.core.models.Gear as CoreGear

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

    @SerialName("distanceUsedMeters")
    val distance: Double? = null,
) {
    fun toCore(): CoreGear =
        CoreGear(
            id = id,
            name = name ?: "$brand $model",
            type = toCoreGearType(),
            distance = distance?.roundToInt()
        )

    private fun toCoreGearType(): GearType = when (type) {
        "BIKE" -> GearType.Bike
        "SHOES" -> GearType.Shoe
        else -> GearType.Unknown
    }
}