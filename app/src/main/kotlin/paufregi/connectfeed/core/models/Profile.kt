package paufregi.connectfeed.core.models

data class Profile(
    val id: Long = 0,
    val name: String = "",
    val category: ActivityCategory = ActivityCategory.Any,
    val eventType: EventType? = null,
    val course: Course? = null,
    val water: Int? = null,
    val rename: Boolean = true,
    val customWater: Boolean = false,
    val feelAndEffort: Boolean = false,
    val trainingEffect: Boolean = false,
) {
    fun compatibleWith(activity: Activity): Boolean =
        this.category == ActivityCategory.Any || this.category.compatibleWith(activity)
}
