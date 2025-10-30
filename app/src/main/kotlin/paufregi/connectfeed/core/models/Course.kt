package paufregi.connectfeed.core.models

data class Course(
    val id: Long,
    val name: String,
    val distance: Double,
    val type: ActivityType
) {
    fun compatibleWith(activity: Activity?): Boolean =
        activity == null || ActivityCategory.findCategory(this.type).compatibleWith(activity)

    fun compatibleWith(profile: Profile?): Boolean =
        profile == null || profile.compatibleWith(this)
}
