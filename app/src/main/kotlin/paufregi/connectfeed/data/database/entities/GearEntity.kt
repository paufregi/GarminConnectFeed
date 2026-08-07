package paufregi.connectfeed.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import paufregi.connectfeed.core.models.GearType

@Entity(
    tableName = "gears",
    foreignKeys = [
        ForeignKey(
            entity = StravaGearEntity::class,
            parentColumns = ["id"],
            childColumns = ["stravaGearId"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("stravaGearId")]
)
data class GearEntity(
    @PrimaryKey
    val id: String,

    val userId: Long,
    val name: String,
    val type: GearType = GearType.Unknown,
    val distance: Long? = 0,

    @ColumnInfo(defaultValue = "NULL")
    val stravaGearId: String? = null,
)
