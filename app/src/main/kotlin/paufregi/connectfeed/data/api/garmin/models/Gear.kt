package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.api.garmin.serializers.GearTypeSerializer

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
    @Serializable(with = GearTypeSerializer::class)
    val type: GearType,
    @SerialName("distanceUsedMeters")
    val distance: Double? = null,
)