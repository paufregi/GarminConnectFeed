package paufregi.connectfeed.data.database.coverters

import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.data.database.entities.StravaGearEntity

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

fun Gear.toStravaEntity(athleteId: Long) = StravaGearEntity(
    id = id,
    athleteId = athleteId,
    name = name,
    type = type,
    distance = distance
)

fun StravaGearEntity.toCore() = Gear(
    id = id,
    name = name,
    type = type,
    distance = distance
)
