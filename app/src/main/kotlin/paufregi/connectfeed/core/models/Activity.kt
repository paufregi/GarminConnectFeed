package paufregi.connectfeed.core.models

import java.time.Instant

data class Activity(
    val id: Long,
    val name: String,
    val type: ActivityType,
    val eventType: EventType? = null,
    val distance: Double? = null,
    val trainingEffect: String? = null,
    val date: Instant? = null
) {
    fun match(other: Activity): Boolean {
        return this.type == other.type && this.date == other.date
    }
}