package paufregi.connectfeed.data.database.coverters

import androidx.room.TypeConverter
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverter {
    @TypeConverter
    fun fromString(type: String?): ActivityType = when (type) {
        ActivityType.Any.name -> ActivityType.Any
        ActivityType.Running.name -> ActivityType.Running
        ActivityType.TrailRunning.name -> ActivityType.TrailRunning
        ActivityType.TreadmillRunning.name -> ActivityType.TreadmillRunning
        ActivityType.Cycling.name -> ActivityType.Cycling
        ActivityType.IndoorCycling.name -> ActivityType.IndoorCycling
        ActivityType.EBiking.name -> ActivityType.EBiking
        ActivityType.Swimming.name -> ActivityType.Swimming
        ActivityType.OpenWaterSwimming.name -> ActivityType.OpenWaterSwimming
        ActivityType.Strength.name -> ActivityType.Strength
        ActivityType.Walking.name -> ActivityType.Walking
        ActivityType.Hiking.name -> ActivityType.Hiking
        ActivityType.Rugby.name -> ActivityType.Rugby
        ActivityType.Soccer.name -> ActivityType.Soccer
        ActivityType.JumpRope.name -> ActivityType.JumpRope
        ActivityType.Breathwork.name -> ActivityType.Breathwork
        ActivityType.Meditation.name -> ActivityType.Meditation
        ActivityType.Yoga.name -> ActivityType.Yoga
        ActivityType.Other.name -> ActivityType.Other
        else -> ActivityType.Unknown
    }

    @TypeConverter
    fun toString(type: ActivityType?): String? = type?.name
}
