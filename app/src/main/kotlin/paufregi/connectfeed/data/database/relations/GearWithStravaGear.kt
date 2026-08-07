package paufregi.connectfeed.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.data.database.entities.StravaGearEntity

data class GearWithStravaGear(
    @Embedded val gear: GearEntity,

    @Relation(
        parentColumn = "stravaGearId",
        entityColumn = "id"
    )
    val stravaGear: StravaGearEntity?,
)
