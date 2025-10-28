package paufregi.connectfeed.core.models

sealed interface ActivityType {
    // GARMIN

    // Running
    data object Running: ActivityType // 1
    data object TrackRunning: ActivityType // 8
    data object TrailRunning: ActivityType // 6
    data object TreadmillRunning: ActivityType // 18
    data object UltraRun: ActivityType // 181

    // Cycling
    data object Cycling: ActivityType // 2
    data object DownhillBiking: ActivityType // 20
    data object EBiking: ActivityType // 176
    data object EBikingMountain: ActivityType // 175
    data object GravelCycling: ActivityType // 143
    data object MountainBiking: ActivityType // 5
    data object RoadBiking: ActivityType // 10
    data object VirtualRide: ActivityType // 153

    // Fitness
    data object HIIT: ActivityType // 180
    data object Breathwork: ActivityType // 164
    data object Cardio: ActivityType // 11
    data object JumpRope: ActivityType // 254
    data object StrengthTraining: ActivityType // 13
    data object Yoga: ActivityType // 163

    // Swimming
    data object Swimming: ActivityType
    data object PoolSwimming: ActivityType // 27
    data object OpenWaterSwimming: ActivityType // 28

    // Other
    data object Walking: ActivityType // 9
    data object Hiking: ActivityType // 3

    // Other
    data object Snowboarding: ActivityType // 252
    data object Kayaking: ActivityType // 231
    data object StandUpPaddling: ActivityType // 239
    data object Surfing: ActivityType // 240
    data object Windsurf: ActivityType // 242

    // Other
    data object Football: ActivityType // 215
    data object MultiSport: ActivityType // 89
    data object Other: ActivityType // 4

    // STRAVA
    // Running
    data object StravaRunning: ActivityType // Run
    data object StravaTrailRun: ActivityType // TrailRun

    // Cycling
    data object StravaRide: ActivityType // Ride
    data object StravaMountainBikeRide: ActivityType // MountainBikeRide
    data object StravaGravelRide: ActivityType // GravelRide
    data object StravaEBikeRide: ActivityType // EBikeRide
    data object StravaEMountainBikeRide: ActivityType // EMountainBikeRide
    data object StravaVirtualRide: ActivityType // VirtualRide

    // Fitness
    data object StravaHIIT: ActivityType // HighIntensityIntervalTraining
    data object StravaWorkout: ActivityType // Workout
    data object StravaWeightTraining: ActivityType // WeightTraining
    data object StravaYoga: ActivityType // Yoga

    // Swimming
    data object StravaSwim: ActivityType // Swim

    // Other
    data object StravaWalk: ActivityType // Walk
    data object StravaHike: ActivityType // Hike

    // Other
    data object StravaSnowboard: ActivityType // Snowboard
    data object StravaKayaking: ActivityType // Kayaking
    data object StravaStandUpPaddling: ActivityType // StandUpPaddling
    data object StravaSurfing: ActivityType // Surfing
    data object StravaWindsurf: ActivityType // Windsurf

    // Other
    data object StravaFootball: ActivityType // Soccer
}

