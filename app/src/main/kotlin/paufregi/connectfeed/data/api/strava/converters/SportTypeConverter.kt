package paufregi.connectfeed.data.api.strava.converters

import paufregi.connectfeed.core.models.ActivityType

object SportTypeConverter {
    fun toActivityType(type: String): ActivityType = when (type) {
        "AlpineSki" -> ActivityType.StravaAlpineSki
        "BackcountrySki" -> ActivityType.StravaBackcountrySki
        "Badminton" -> ActivityType.StravaBadminton
        "Canoeing" -> ActivityType.StravaCanoeing
        "Crossfit" -> ActivityType.StravaCrossfit
        "EBikeRide" -> ActivityType.StravaEBikeRide
        "Elliptical" -> ActivityType.StravaElliptical
        "EMountainBikeRide" -> ActivityType.StravaEMountainBikeRide
        "Golf" -> ActivityType.StravaGolf
        "GravelRide" -> ActivityType.StravaGravelRide
        "Handcycle" -> ActivityType.StravaHandcycle
        "HighIntensityIntervalTraining" -> ActivityType.StravaHighIntensityIntervalTraining
        "Hike" -> ActivityType.StravaHike
        "IceSkate" -> ActivityType.StravaIceSkate
        "InlineSkate" -> ActivityType.StravaIceSkate
        "Kayaking" -> ActivityType.StravaKayaking
        "Kitesurf" -> ActivityType.StravaKitesurf
        "MountainBikeRide" -> ActivityType.StravaMountainBikeRide
        "NordicSki" -> ActivityType.StravaNordicSki
        "Pickleball" -> ActivityType.StravaPickleball
        "Pilates" -> ActivityType.StravaPilates
        "Racquetball" -> ActivityType.StravaRacquetball
        "Ride" -> ActivityType.StravaRide
        "RockClimbing" -> ActivityType.StravaRockClimbing
        "RollerSki" -> ActivityType.StravaRollerSki
        "Rowing" -> ActivityType.StravaRowing
        "Run" -> ActivityType.StravaRun
        "Sail" -> ActivityType.StravaSail
        "Skateboard" -> ActivityType.StravaSkateboard
        "Snowboard" -> ActivityType.StravaSnowboard
        "Snowshoe" -> ActivityType.StravaSnowshoe
        "Soccer" -> ActivityType.StravaSoccer
        "Squash" -> ActivityType.StravaSquash
        "StairStepper" -> ActivityType.StravaStairStepper
        "StandUpPaddling" -> ActivityType.StravaStandUpPaddling
        "Surfing" -> ActivityType.StravaSurfing
        "Swim" -> ActivityType.StravaSwim
        "TableTennis" -> ActivityType.StravaTableTennis
        "Tennis" -> ActivityType.StravaTennis
        "TrailRun" -> ActivityType.StravaTrailRun
        "Velomobile" -> ActivityType.StravaVelomobile
        "VirtualRide" -> ActivityType.StravaVirtualRide
        "VirtualRow" -> ActivityType.StravaVirtualRow
        "VirtualRun" -> ActivityType.StravaVirtualRun
        "Walk" -> ActivityType.StravaWalk
        "WeightTraining" -> ActivityType.StravaWeightTraining
        "Wheelchair" -> ActivityType.StravaWheelchair
        "Windsurf" -> ActivityType.StravaWindsurf
        "Workout" -> ActivityType.StravaWorkout
        "Yoga" -> ActivityType.StravaYoga
        else -> ActivityType.Unknown
    }
}