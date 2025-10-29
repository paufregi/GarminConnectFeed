package paufregi.connectfeed.core.models

data class Course(
    val id: Long,
    val name: String,
    val distance: Double,
    val type: ActivityType
) {
    fun compatibleWith(profile: Profile?): Boolean =
        profile != null && profile.compatibleWith(this)

    fun compatibleWith(activity: Activity?): Boolean =
        activity != null && this.type == activity.type
}
