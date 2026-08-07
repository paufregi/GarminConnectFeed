package paufregi.connectfeed.data.database.coverters

import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.database.entities.StravaGearEntity
import paufregi.connectfeed.data.api.strava.models.Gear as StravaGear

fun StravaGear.toEntity(type: GearType, stravaAthleteId: Long) = StravaGearEntity(
    id = id,
    stravaAthleteId = stravaAthleteId,
    name = name,
    type = type,
)

fun StravaGearEntity.toCore() = StravaGear(
    id = id,
    name = name,
)
