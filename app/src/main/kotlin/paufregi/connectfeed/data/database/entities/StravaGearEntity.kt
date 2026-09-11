package paufregi.connectfeed.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import paufregi.connectfeed.core.models.GearType

@Entity(tableName = "strava_gears")
data class StravaGearEntity(
    @PrimaryKey
    val id: String,

    val athleteId: Long,
    val name: String,
    val type: GearType = GearType.Unknown,
    val distance: Int? = null,
)

