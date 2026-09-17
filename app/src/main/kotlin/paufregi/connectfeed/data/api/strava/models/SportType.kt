package paufregi.connectfeed.data.api.strava.models

import paufregi.connectfeed.core.models.ActivityType

sealed class SportType(val key: String, val type: ActivityType? = null){
    data object Run: SportType("Run", ActivityType.Running) // Run
    data object TrailRun: SportType("TrailRun", ActivityType.Running) // TrailRun

    data object Ride: SportType("Ride", ActivityType.Cycling) // Ride
    data object MountainBikeRide: SportType("MountainBikeRide", ActivityType.Cycling) // MountainBikeRide
    data object GravelRide: SportType("GravelRide", ActivityType.Cycling) // GravelRide
    data object EBikeRide: SportType("EBikeRide", ActivityType.Cycling) // EBikeRide
    data object EMountainBikeRide: SportType("EMountainBikeRide", ActivityType.Cycling) // EMountainBikeRide
    data object VirtualRide: SportType("VirtualRide", ActivityType.Cycling) // VirtualRide

    // Fitness
    data object HIIT: SportType("HighIntensityIntervalTraining", ActivityType.Fitness) // HighIntensityIntervalTraining
    data object Workout: SportType("Workout", ActivityType.Fitness) // Workout
    data object WeightTraining: SportType("WeightTraining", ActivityType.Fitness) // WeightTraining
    data object Yoga: SportType("Yoga", ActivityType.Fitness) // Yoga

    // Swimming
    data object Swim: SportType("Swim", ActivityType.Swimming) // Swim

    // Other
    data object Walk: SportType("Walk", ActivityType.Other) // Walk
    data object Hike: SportType("Hike", ActivityType.Other) // Hike

    // Other
    data object Snowboard: SportType("Snowboard", ActivityType.Other) // Snowboard
    data object Kayaking: SportType("Kayaking", ActivityType.Other) // Kayaking
    data object StandUpPaddling: SportType("StandUpPaddling", ActivityType.Other) // StandUpPaddling
    data object Surfing: SportType("Surfing", ActivityType.Other) // Surfing
    data object Windsurf: SportType("Windsurf", ActivityType.Other) // Windsurf

    // Other
    data object Soccer: SportType("Soccer", ActivityType.Other) // Soccer
    data object Unknown : SportType("Unknown", ActivityType.Other)
}
