package paufregi.connectfeed.core.models

data class Activity(
    val id: Long,
    val name: String,
    val type: ActivityType,
    val eventType: EventType? = null,
    val distance: Double? = null,
    val trainingEffect: String? = null,
)