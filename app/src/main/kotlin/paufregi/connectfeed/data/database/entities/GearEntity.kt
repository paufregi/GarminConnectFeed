package paufregi.connectfeed.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import paufregi.connectfeed.core.models.GearType

@Entity(tableName = "gears")
data class GearEntity(
    @PrimaryKey
    val id: String,

    val userId: Long,
    val name: String,
    val type: GearType = GearType.Unknown,
    val distance: Long? = 0
)
