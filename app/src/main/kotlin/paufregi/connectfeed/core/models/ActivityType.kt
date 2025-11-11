package paufregi.connectfeed.core.models

sealed class ActivityType(val name: String, val parent: ActivityType? = null) {

    data object Any : ActivityType("Any")
    data object Other: ActivityType("Other")
    data object Fitness: ActivityType("Fitness")
    data object Unknown : ActivityType("Unknown")

    // GARMIN
    // Running
    data object Running: ActivityType("Running") // 1 - parent for running
    data object TrackRunning: ActivityType("Track Running", Running) // 8
    data object TrailRunning: ActivityType("Trail Running", Running) // 6
    data object TreadmillRunning: ActivityType("Treadmill Running", Running) // 18
    data object UltraRun: ActivityType("Ultra Run", Running) // 181

    // Cycling
    data object Cycling: ActivityType("Cycling") // 2 - parent for running
    data object DownhillBiking: ActivityType("Downhill Biking", Cycling) // 20
    data object EBiking: ActivityType("E Biking", Cycling) // 176
    data object EBikingMountain: ActivityType("E Biking Mountain", Cycling) // 175
    data object GravelCycling: ActivityType("Gravel Cycling", Cycling) // 143
    data object MountainBiking: ActivityType("Mountain Biking", Cycling) // 5
    data object RoadBiking: ActivityType("Road Biking", Cycling) // 10
    data object VirtualRide: ActivityType("Virtual Ride", Cycling) // 153

    // Fitness
    data object HIIT: ActivityType("HIIT", Fitness) // 180
    data object Breathwork: ActivityType("Breathwork", Fitness) // 164
    data object Cardio: ActivityType("Cardio", Fitness) // 11
    data object JumpRope: ActivityType("Jump Rope", Fitness) // 254
    data object StrengthTraining: ActivityType("Strength Training", Fitness) // 13
    data object Yoga: ActivityType("Yoga", Fitness) // 163

    // Swimming
    data object Swimming: ActivityType("Swimming") // 26
    data object PoolSwimming: ActivityType("Pool Swimming", Swimming) // 27
    data object OpenWaterSwimming: ActivityType("Open Water Swimming", Swimming) // 28

    // Other
    data object Walking: ActivityType("Walking", Other) // 9
    data object Hiking: ActivityType("Hiking", Other) // 3

    // Other
    data object Snowboarding: ActivityType("Snowboarding", Other) // 252
    data object Kayaking: ActivityType("Kayaking", Other) // 231
    data object StandUpPaddling: ActivityType("Stand Up Paddling", Other) // 239
    data object Surfing: ActivityType("Surfing", Other) // 240
    data object Windsurf: ActivityType("Windsurf", Other) // 242

    // STRAVA
    // Running
    data object StravaRunning: ActivityType("Strava Running", Running) // Run
    data object StravaTrailRun: ActivityType("Strava Trail Run", Running) // TrailRun

    // Cycling
    data object StravaRide: ActivityType("Ride", Cycling) // Ride
    data object StravaMountainBikeRide: ActivityType("Mountain Bike Ride", Cycling) // MountainBikeRide
    data object StravaGravelRide: ActivityType("Gravel Ride", Cycling) // GravelRide
    data object StravaEBikeRide: ActivityType("E Bike Ride", Cycling) // EBikeRide
    data object StravaEMountainBikeRide: ActivityType("E Mountain Bike Ride", Cycling) // EMountainBikeRide
    data object StravaVirtualRide: ActivityType("Virtual Ride", Cycling) // VirtualRide

    // Fitness
    data object StravaHIIT: ActivityType("HIIT", Fitness) // HighIntensityIntervalTraining
    data object StravaWorkout: ActivityType("Workout", Fitness) // Workout
    data object StravaWeightTraining: ActivityType("Weight Training", Fitness) // WeightTraining
    data object StravaYoga: ActivityType("Yoga", Fitness) // Yoga

    // Swimming
    data object StravaSwim: ActivityType("Swim", Swimming) // Swim

    // Other
    data object StravaWalk: ActivityType("Walk", Other) // Walk
    data object StravaHike: ActivityType("Hike", Other) // Hike

    // Other
    data object StravaSnowboard: ActivityType("Snowboard", Other) // Snowboard
    data object StravaKayaking: ActivityType("Kayaking", Other) // Kayaking
    data object StravaStandUpPaddling: ActivityType("Stand Up Paddling", Other) // StandUpPaddling
    data object StravaSurfing: ActivityType("Surfing", Other) // Surfing
    data object StravaWindsurf: ActivityType("Windsurf", Other) // Windsurf

    // Other
    data object StravaFootball: ActivityType("Football", Other) // Soccer

    val allowCourse: Boolean
        get() = (parent ?: this) in setOf(Running, Cycling)

    val order: Int
        get() = when (parent ?: this) {
            Any -> 0
            Running -> 1
            Cycling -> 2
            Swimming -> 3
            Fitness -> 4
            Other -> 5
            Unknown -> 6
            else -> 7
        }

    fun compatible(other: ActivityType): Boolean =
        this == Any || other == Any || (parent ?: this) == (other.parent ?: other)

}
