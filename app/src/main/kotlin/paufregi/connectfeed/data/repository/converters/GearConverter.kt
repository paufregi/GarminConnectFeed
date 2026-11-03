package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.api.garmin.models.GearDto
import paufregi.connectfeed.data.database.entities.GearEntity
import kotlin.math.roundToInt

fun Gear.toEntity(userId: Long) = GearEntity(
    id = id,
    userId = userId,
    name = name,
    type = type,
    distance = distance
)

fun GearEntity.toCore() = Gear(
    id = id,
    name = name,
    type = type,
    distance = distance
)

fun GearDto.toCore() = Gear(
    id = id,
    name = name ?: "$brand $model",
    type = when (type) {
        "BIKE" -> GearType.Bike
        "SHOES" -> GearType.Shoe
        else -> GearType.Unknown
    },
    distance = distance?.roundToInt()
)