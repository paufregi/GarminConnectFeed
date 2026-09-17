package paufregi.connectfeed.data.repository.utils

import paufregi.connectfeed.core.models.GearType

object GearTypeConverter {

    fun garmin(type: String): GearType = when (type) {
        "BIKE" -> GearType.Bike
        "SHOES" -> GearType.Shoe
        else -> GearType.Unknown
    }
}