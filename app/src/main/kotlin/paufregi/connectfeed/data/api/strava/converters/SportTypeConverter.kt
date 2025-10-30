package paufregi.connectfeed.data.api.strava.converters

import paufregi.connectfeed.core.models.ActivityType

object SportTypeConverter {
    fun toActivityType(type: String): ActivityType = when (type) {
        "EBikeRide" -> ActivityType.StravaEBikeRide
        "EMountainBikeRide" -> ActivityType.StravaEMountainBikeRide
        "GravelRide" -> ActivityType.StravaGravelRide
        "HighIntensityIntervalTraining" -> ActivityType.StravaHIIT
        "Hike" -> ActivityType.Hiking
        "Kayaking" -> ActivityType.StravaKayaking
        "MountainBikeRide" -> ActivityType.StravaMountainBikeRide
        "Ride" -> ActivityType.StravaRide
        "Run" -> ActivityType.StravaRunning
        "Snowboard" -> ActivityType.StravaSnowboard
        "Soccer" -> ActivityType.StravaFootball
        "StandUpPaddling" -> ActivityType.StravaStandUpPaddling
        "Surfing" -> ActivityType.StravaSurfing
        "Swim" -> ActivityType.Swimming
        "TrailRun" -> ActivityType.StravaTrailRun
        "VirtualRide" -> ActivityType.StravaVirtualRide
        "Walk" -> ActivityType.StravaWalk
        "WeightTraining" -> ActivityType.StravaWeightTraining
        "Windsurf" -> ActivityType.StravaWindsurf
        "Workout" -> ActivityType.StravaWorkout
        "Yoga" -> ActivityType.Yoga
        else -> ActivityType.Other
    }
}