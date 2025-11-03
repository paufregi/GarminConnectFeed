package paufregi.connectfeed.core.models

sealed class ActivityType(val profileType: ProfileType) {
    // GARMIN
    // Running
    data object Running: ActivityType(ProfileType.Running) // 1
    data object TrackRunning: ActivityType(ProfileType.Running) // 8
    data object TrailRunning: ActivityType(ProfileType.Running) // 6
    data object TreadmillRunning: ActivityType(ProfileType.Running) // 18
    data object UltraRun: ActivityType(ProfileType.Running) // 181

    // Cycling
    data object Cycling: ActivityType(ProfileType.Cycling) // 2
    data object DownhillBiking: ActivityType(ProfileType.Cycling) // 20
    data object EBiking: ActivityType(ProfileType.Cycling) // 176
    data object EBikingMountain: ActivityType(ProfileType.Cycling) // 175
    data object GravelCycling: ActivityType(ProfileType.Cycling) // 143
    data object MountainBiking: ActivityType(ProfileType.Cycling) // 5
    data object RoadBiking: ActivityType(ProfileType.Cycling) // 10
    data object VirtualRide: ActivityType(ProfileType.Cycling) // 153

    // Fitness
    data object HIIT: ActivityType(ProfileType.Fitness) // 180
    data object Breathwork: ActivityType(ProfileType.Fitness) // 164
    data object Cardio: ActivityType(ProfileType.Fitness) // 11
    data object JumpRope: ActivityType(ProfileType.Fitness) // 254
    data object StrengthTraining: ActivityType(ProfileType.Fitness) // 13
    data object Yoga: ActivityType(ProfileType.Fitness) // 163

    // Swimming
    data object Swimming: ActivityType(ProfileType.Swimming)
    data object PoolSwimming: ActivityType(ProfileType.Swimming) // 27
    data object OpenWaterSwimming: ActivityType(ProfileType.Swimming) // 28

    // Other
    data object Walking: ActivityType(ProfileType.Other) // 9
    data object Hiking: ActivityType(ProfileType.Other) // 3

    // Other
    data object Snowboarding: ActivityType(ProfileType.Other) // 252
    data object Kayaking: ActivityType(ProfileType.Other) // 231
    data object StandUpPaddling: ActivityType(ProfileType.Other) // 239
    data object Surfing: ActivityType(ProfileType.Other) // 240
    data object Windsurf: ActivityType(ProfileType.Other) // 242

    // STRAVA
    // Running
    data object StravaRunning: ActivityType(ProfileType.Running) // Run
    data object StravaTrailRun: ActivityType(ProfileType.Running) // TrailRun

    // Cycling
    data object StravaRide: ActivityType(ProfileType.Cycling) // Ride
    data object StravaMountainBikeRide: ActivityType(ProfileType.Cycling) // MountainBikeRide
    data object StravaGravelRide: ActivityType(ProfileType.Cycling) // GravelRide
    data object StravaEBikeRide: ActivityType(ProfileType.Cycling) // EBikeRide
    data object StravaEMountainBikeRide: ActivityType(ProfileType.Cycling) // EMountainBikeRide
    data object StravaVirtualRide: ActivityType(ProfileType.Cycling) // VirtualRide

    // Fitness
    data object StravaHIIT: ActivityType(ProfileType.Fitness) // HighIntensityIntervalTraining
    data object StravaWorkout: ActivityType(ProfileType.Fitness) // Workout
    data object StravaWeightTraining: ActivityType(ProfileType.Fitness) // WeightTraining
    data object StravaYoga: ActivityType(ProfileType.Fitness) // Yoga

    // Swimming
    data object StravaSwim: ActivityType(ProfileType.Swimming) // Swim

    // Other
    data object StravaWalk: ActivityType(ProfileType.Other) // Walk
    data object StravaHike: ActivityType(ProfileType.Other) // Hike

    // Other
    data object StravaSnowboard: ActivityType(ProfileType.Other) // Snowboard
    data object StravaKayaking: ActivityType(ProfileType.Other) // Kayaking
    data object StravaStandUpPaddling: ActivityType(ProfileType.Other) // StandUpPaddling
    data object StravaSurfing: ActivityType(ProfileType.Other) // Surfing
    data object StravaWindsurf: ActivityType(ProfileType.Other) // Windsurf

    // Other
    data object StravaFootball: ActivityType(ProfileType.Other) // Soccer

    data object Unknown : ActivityType(ProfileType.Other)
}
