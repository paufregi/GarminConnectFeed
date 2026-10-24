package paufregi.connectfeed.core.models

data class Course(
    val id: Long,
    val name: String,
    val distance: Double,
    val type: ActivityType
) {
    fun compatible(activity: Activity): Boolean =
        this.type.compatible(activity.type)
}
