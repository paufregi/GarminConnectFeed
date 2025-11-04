package paufregi.connectfeed.data.database.coverters

import androidx.room.TypeConverter
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverter {
    @TypeConverter
    fun fromName(type: String?): ActivityType = when (type) {
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
        ActivityType.HIIT::class.simpleName -> ActivityType.HIIT
        ActivityType.Breathwork::class.simpleName -> ActivityType.Breathwork
        ActivityType.Cardio::class.simpleName -> ActivityType.Cardio
        ActivityType.JumpRope::class.simpleName -> ActivityType.JumpRope
        ActivityType.StrengthTraining::class.simpleName -> ActivityType.StrengthTraining
        ActivityType.Yoga::class.simpleName -> ActivityType.Yoga
        ActivityType.Swimming::class.simpleName -> ActivityType.Swimming
        ActivityType.PoolSwimming::class.simpleName -> ActivityType.PoolSwimming
        ActivityType.OpenWaterSwimming::class.simpleName -> ActivityType.OpenWaterSwimming
        ActivityType.Walking::class.simpleName -> ActivityType.Walking
        ActivityType.Hiking::class.simpleName -> ActivityType.Hiking
        ActivityType.Snowboarding::class.simpleName -> ActivityType.Snowboarding
        ActivityType.Kayaking::class.simpleName -> ActivityType.Kayaking
        ActivityType.StandUpPaddling::class.simpleName -> ActivityType.StandUpPaddling
        ActivityType.Surfing::class.simpleName -> ActivityType.Surfing
        ActivityType.Windsurf::class.simpleName -> ActivityType.Windsurf
        ActivityType.StravaRunning::class.simpleName -> ActivityType.StravaRunning
        ActivityType.StravaTrailRun::class.simpleName -> ActivityType.StravaTrailRun
        ActivityType.StravaRide::class.simpleName -> ActivityType.StravaRide
        ActivityType.StravaMountainBikeRide::class.simpleName -> ActivityType.StravaMountainBikeRide
        ActivityType.StravaGravelRide::class.simpleName -> ActivityType.StravaGravelRide
        ActivityType.StravaEBikeRide::class.simpleName -> ActivityType.StravaEBikeRide
        ActivityType.StravaEMountainBikeRide::class.simpleName -> ActivityType.StravaEMountainBikeRide
        ActivityType.StravaVirtualRide::class.simpleName -> ActivityType.StravaVirtualRide
        ActivityType.StravaHIIT::class.simpleName -> ActivityType.StravaHIIT
        ActivityType.StravaWorkout::class.simpleName -> ActivityType.StravaWorkout
        ActivityType.StravaWeightTraining::class.simpleName -> ActivityType.StravaWeightTraining
        ActivityType.StravaYoga::class.simpleName -> ActivityType.StravaYoga
        ActivityType.StravaSwim::class.simpleName -> ActivityType.StravaSwim
        ActivityType.StravaWalk::class.simpleName -> ActivityType.StravaWalk
        ActivityType.StravaHike::class.simpleName -> ActivityType.StravaHike
        ActivityType.StravaSnowboard::class.simpleName -> ActivityType.StravaSnowboard
        ActivityType.StravaKayaking::class.simpleName -> ActivityType.StravaKayaking
        ActivityType.StravaStandUpPaddling::class.simpleName -> ActivityType.StravaStandUpPaddling
        ActivityType.StravaSurfing::class.simpleName -> ActivityType.StravaSurfing
        ActivityType.StravaWindsurf::class.simpleName -> ActivityType.StravaWindsurf
        ActivityType.StravaFootball::class.simpleName -> ActivityType.StravaFootball
        else -> ActivityType.Unknown
    }

    @TypeConverter
    fun toName(type: ActivityType?): String? = type?.javaClass?.simpleName
}
