package paufregi.connectfeed.core.models

sealed class ActivityType(val name: String, val order: Int) {
    data object Any : ActivityType("Any", 1)
    data object Running : ActivityType("Running", 2)
    data object TrailRunning : ActivityType("Trail Running", 3)
    data object TreadmillRunning : ActivityType("Treadmill Running", 4)
    data object Cycling : ActivityType("Cycling", 5)
    data object IndoorCycling : ActivityType("Cycling", 6)
    data object EBiking : ActivityType("eBiking", 7)
    data object Swimming : ActivityType("Swimming", 8)
    data object OpenWaterSwimming : ActivityType("Open Water Swimming", 9)
    data object Strength : ActivityType("Strength", 10)
    data object Walking : ActivityType("Walking", 11)
    data object Hiking : ActivityType("Hiking", 12)
    data object Rugby : ActivityType("Rugby", 13)
    data object Soccer : ActivityType("Soccer", 14)
    data object JumpRope : ActivityType("Jump Rope", 15)
    data object Breathwork : ActivityType("Breathwork", 16)
    data object Meditation : ActivityType("Meditation", 17)
    data object Yoga : ActivityType("Yoga", 18)
    data object Other : ActivityType("Other", 99)
    data object Unknown : ActivityType("Unknown", 100)
}
