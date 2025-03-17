package paufregi.connectfeed.core.models

sealed class ActivityType(val name: String, val order: Int, val allowCourseInProfile: Boolean) {
    data object Any : ActivityType("Any", 1, false)
    data object Running : ActivityType("Running", 2, true)
    data object TrailRunning : ActivityType("Trail Running", 3, true)
    data object TreadmillRunning : ActivityType("Treadmill Running", 4, false)
    data object Cycling : ActivityType("Cycling", 5, true)
    data object IndoorCycling : ActivityType("Indoor Cycling", 6, false)
    data object EBiking : ActivityType("eBiking", 7, true)
    data object Swimming : ActivityType("Swimming", 8, true)
    data object OpenWaterSwimming : ActivityType("Open Water Swimming", 9, false)
    data object Strength : ActivityType("Strength", 10, false)
    data object Walking : ActivityType("Walking", 11, true)
    data object Hiking : ActivityType("Hiking", 12, true)
    data object Rugby : ActivityType("Rugby", 13, false)
    data object Soccer : ActivityType("Soccer", 14, false)
    data object JumpRope : ActivityType("Jump Rope", 15, false)
    data object Breathwork : ActivityType("Breathwork", 16, false)
    data object Meditation : ActivityType("Meditation", 17, false)
    data object Yoga : ActivityType("Yoga", 18, false)
    data object Other : ActivityType("Other", 99, false)
    data object Unknown : ActivityType("Unknown", 100, false)
}
