package paufregi.connectfeed.core.models

data class Activity(
    val id: Long,
    val name: String,
    val distance: Double?,
    val type: ActivityType,
)