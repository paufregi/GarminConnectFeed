package paufregi.connectfeed.core.models

import java.time.Duration
import java.time.Instant

data class Activity(
    val id: Long,
    val name: String,
    val type: ActivityType,
    val category:
    val eventType: EventType? = null,
    val distance: Double? = null,
    val trainingEffect: String? = null,
    val date: Instant? = null
) {
    fun match(other: Activity): Boolean =
        this.type == other.type && (this.date != null && other.date != null && Duration.between(this.date, other.date).abs() <= Duration.ofMinutes(1))
}
