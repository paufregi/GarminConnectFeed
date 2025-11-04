package paufregi.connectfeed.data.api.strava.converters

import paufregi.connectfeed.core.models.ActivityType

object SportTypeConverter {
    fun toActivityType(type: String): ActivityType = when (type) {
        // Running
        "Run" -> ActivityType.StravaRunning
        "TrailRun" -> ActivityType.StravaTrailRun
        "VirtualRun" -> ActivityType.StravaRunning

        // Cycling
        "Ride" -> ActivityType.StravaRide
        "MountainBikeRide" -> ActivityType.StravaMountainBikeRide
        "GravelRide" -> ActivityType.StravaGravelRide
        "EBikeRide" -> ActivityType.StravaEBikeRide
        "EMountainBikeRide" -> ActivityType.StravaEMountainBikeRide
        "VirtualRide" -> ActivityType.StravaVirtualRide

        // Fitness
        "HighIntensityIntervalTraining" -> ActivityType.StravaHIIT
        "Workout" -> ActivityType.StravaWorkout
        "WeightTraining" -> ActivityType.StravaWeightTraining
        "Yoga" -> ActivityType.StravaYoga

        // Swimming
        "Swim" -> ActivityType.StravaSwim

        // Other
        "Walk" -> ActivityType.StravaWalk
        "Hike" -> ActivityType.StravaHike
        "Snowboard" -> ActivityType.StravaSnowboard
        "Kayaking" -> ActivityType.StravaKayaking
        "StandUpPaddling" -> ActivityType.StravaStandUpPaddling
        "Surfing" -> ActivityType.StravaSurfing
        "Windsurf" -> ActivityType.StravaWindsurf
        "Soccer" -> ActivityType.StravaFootball

        else -> ActivityType.Unknown
    }
}