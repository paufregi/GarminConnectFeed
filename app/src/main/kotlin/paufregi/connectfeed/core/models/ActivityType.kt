package paufregi.connectfeed.core.models

sealed class ActivityType(val name: String, val parent: ActivityType? = null) {

    data object Any : ActivityType("Any")
    data object Other: ActivityType("Other")
    data object Unknown : ActivityType("Unknown")

    // GARMIN
    // Running
    data object Running: ActivityType("Running") // 1 - parent for running
    data object TrackRunning: ActivityType("Track Running", Running) // 8
    data object TrailRunning: ActivityType("Trail Running", Running) // 6
    data object TreadmillRunning: ActivityType("Treadmill Running", Running) // 18
    data object UltraRun: ActivityType("Ultra Run", Running) // 181

    // Cycling
    data object Cycling: ActivityType("Cycling") // 2 - parent for cycling
    data object DownhillBiking: ActivityType("Downhill Biking", Cycling) // 20
    data object EBiking: ActivityType("E Biking", Cycling) // 176
    data object EBikingMountain: ActivityType("E Biking Mountain", Cycling) // 175
    data object GravelCycling: ActivityType("Gravel Cycling", Cycling) // 143
    data object MountainBiking: ActivityType("Mountain Biking", Cycling) // 5
    data object RoadBiking: ActivityType("Road Biking", Cycling) // 10
    data object IndoorRide: ActivityType("Indoor Cycling", Cycling) // 25
    data object VirtualRide: ActivityType("Virtual Ride", Cycling) // 152

    // Swimming
    data object Swimming: ActivityType("Swimming") // 26 - parent for swimming
    data object PoolSwimming: ActivityType("Pool Swimming", Swimming) // 27
    data object OpenWaterSwimming: ActivityType("Open Water Swimming", Swimming) // 28

    // Multisport
    data object Multisport: ActivityType("Multisport") // 89

    // Fitness
    data object Fitness: ActivityType("Fitness") // parent for fitness
    data object HIIT: ActivityType("HIIT", Fitness) // 180
    data object Breathwork: ActivityType("Breathwork", Fitness) // 164
    data object Cardio: ActivityType("Cardio", Fitness) // 11
    data object JumpRope: ActivityType("Jump Rope", Fitness) // 254
    data object StrengthTraining: ActivityType("Strength Training", Fitness) // 13
    data object Yoga: ActivityType("Yoga", Fitness) // 163

    // Other
    data object Walking: ActivityType("Walking", Other) // 9
    data object Hiking: ActivityType("Hiking", Other) // 3

    // Other
    data object Snowboarding: ActivityType("Snowboarding", Other) // 252
    data object Kayaking: ActivityType("Kayaking", Other) // 231
    data object StandUpPaddling: ActivityType("Stand Up Paddling", Other) // 239
    data object Surfing: ActivityType("Surfing", Other) // 240
    data object Windsurf: ActivityType("Windsurf", Other) // 242

    val allowCourse: Boolean
        get() = (parent ?: this) in setOf(Running, Cycling)

    val order: Int
        get() = when (parent ?: this) {
            Any -> 0
            Running -> 1
            Cycling -> 2
            Swimming -> 3
            Multisport -> 4
            Fitness -> 5
            Other -> 6
            Unknown -> 7
            else -> 8
        }

    private fun isWildcard(type: ActivityType) = type == Any || type == Unknown

    fun compatible(other: ActivityType): Boolean =
        isWildcard(this) || isWildcard(other) || (parent ?: this) == (other.parent ?: other)
}
