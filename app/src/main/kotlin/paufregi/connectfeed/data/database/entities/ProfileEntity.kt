package paufregi.connectfeed.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.ProfileType

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userId: Long,
    val name: String,
    @ColumnInfo(defaultValue = "Any")
    val type: ProfileType = ProfileType.Any,
    val eventType: EventType? = null,
    @Embedded(prefix = "course_")
    val course: Course? = null,
    val water: Int? = null,
    @ColumnInfo(defaultValue = "true")
    val rename: Boolean = true,
    @ColumnInfo(defaultValue = "false")
    val customWater: Boolean = false,
    @ColumnInfo(defaultValue = "false")
    val feelAndEffort: Boolean = false,
    @ColumnInfo(defaultValue = "false")
    val trainingEffect: Boolean = false,
)
