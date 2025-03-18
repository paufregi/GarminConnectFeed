package paufregi.connectfeed.data.api.strava.converters

import paufregi.connectfeed.core.models.ActivityType

object SportTypeConverter {
    fun toActivityType(type: String): ActivityType = when (type) {
        "AlpineSki" -> ActivityType.Unknown
        "BackcountrySki" -> ActivityType.Unknown
        "Badminton" -> ActivityType.Unknown
        "Canoeing" -> ActivityType.Unknown
        "Crossfit" -> ActivityType.Unknown
        "EBikeRide" -> ActivityType.EBiking
        "Elliptical" -> ActivityType.Unknown
        "EMountainBikeRide" -> ActivityType.EBiking
        "Golf" -> ActivityType.Unknown
        "GravelRide" -> ActivityType.Cycling
        "Handcycle" -> ActivityType.Cycling
        "HighIntensityIntervalTraining" -> ActivityType.Unknown
        "Hike" -> ActivityType.Hiking
        "IceSkate" -> ActivityType.Unknown
        "InlineSkate" -> ActivityType.Unknown
        "Kayaking" -> ActivityType.Unknown
        "Kitesurf" -> ActivityType.Unknown
        "MountainBikeRide" -> ActivityType.Cycling
        "NordicSki" -> ActivityType.Unknown
        "Pickleball" -> ActivityType.Unknown
        "Pilates" -> ActivityType.Unknown
        "Racquetball" -> ActivityType.Unknown
        "Ride" -> ActivityType.Cycling
        "RockClimbing" -> ActivityType.Unknown
        "RollerSki" -> ActivityType.Unknown
        "Rowing" -> ActivityType.Unknown
        "Run" -> ActivityType.Running
        "Sail" -> ActivityType.Unknown
        "Skateboard" -> ActivityType.Unknown
        "Snowboard" -> ActivityType.Unknown
        "Snowshoe" -> ActivityType.Unknown
        "Soccer" -> ActivityType.Soccer
        "Squash" -> ActivityType.Unknown
        "StairStepper" -> ActivityType.Unknown
        "StandUpPaddling" -> ActivityType.Unknown
        "Surfing" -> ActivityType.Unknown
        "Swim" -> ActivityType.Swimming
        "TableTennis" -> ActivityType.Unknown
        "Tennis" -> ActivityType.Unknown
        "TrailRun" -> ActivityType.TrailRunning
        "Velomobile" -> ActivityType.Cycling
        "VirtualRide" -> ActivityType.Cycling
        "VirtualRow" -> ActivityType.Unknown
        "VirtualRun" -> ActivityType.Running
        "Walk" -> ActivityType.Walking
        "WeightTraining" -> ActivityType.Strength
        "Wheelchair" -> ActivityType.Unknown
        "Windsurf" -> ActivityType.Unknown
        "Workout" -> ActivityType.Unknown
        "Yoga" -> ActivityType.Yoga
        else -> ActivityType.Unknown
    }
}