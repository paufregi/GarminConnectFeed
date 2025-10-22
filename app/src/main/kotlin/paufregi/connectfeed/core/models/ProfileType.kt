package paufregi.connectfeed.core.models


sealed class ProfileType(val name: String, val order: Int, val allowCourse: Boolean, val activityTypes: List<ActivityType>) {
    data object Any : ProfileType("Any", 0, false, listOf(ActivityType.StravaVirtualRun))
    data object Running : ProfileType("Running", 1, true, listOf(ActivityType.StravaVirtualRun))
    data object Cycling : ProfileType("Running", 2, true)
    data object Swimming : ProfileType("Swimming", 3, false)
    data object Strength : ProfileType("Strength", 4, false)
    data object Workout : ProfileType("Workout", 5, false)
}
