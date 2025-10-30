package paufregi.connectfeed.data.database.coverters

import androidx.room.TypeConverter
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverter {
    @TypeConverter
    fun fromName(type: String?): ActivityType? =
        when (type) {
            ActivityType.Running::class.simpleName -> ActivityType.Running
            ActivityType.TrackRunning::class.simpleName -> ActivityType.TrackRunning
            ActivityType.TrailRunning::class.simpleName -> ActivityType.TrailRunning
            ActivityType.TreadmillRunning::class.simpleName -> ActivityType.TreadmillRunning
            ActivityType.UltraRun::class.simpleName -> ActivityType.UltraRun
            ActivityType.Cycling::class.simpleName -> ActivityType.Cycling
            ActivityType.DownhillBiking::class.simpleName -> ActivityType.DownhillBiking
            ActivityType.EBiking::class.simpleName -> ActivityType.EBiking
            ActivityType.EBikingMountain::class.simpleName -> ActivityType.EBikingMountain
            ActivityType.GravelCycling::class.simpleName -> ActivityType.GravelCycling
            ActivityType.MountainBiking::class.simpleName -> ActivityType.MountainBiking
            ActivityType.RoadBiking::class.simpleName -> ActivityType.RoadBiking
            ActivityType.VirtualRide::class.simpleName -> ActivityType.VirtualRide
            ActivityType.Walking::class.simpleName -> ActivityType.Walking
            ActivityType.Hiking::class.simpleName -> ActivityType.Hiking
            ActivityType.Other::class.simpleName -> ActivityType.Other
            else -> null
        }

    @TypeConverter
    fun toName(type: ActivityType?): String? = type?.let { it::class.simpleName }
}