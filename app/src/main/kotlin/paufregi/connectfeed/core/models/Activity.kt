package paufregi.connectfeed.core.models

import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

data class Activity(
    val id: Long,
    val name: String,
    val type: ActivityType,
    val eventType: EventType? = null,
    val distance: Double? = null,
    val trainingEffect: String? = null,
    val date: Instant? = null
) {
    fun match(other: Activity): Boolean =
        this.type.compatible(other.type) &&
                (this.date != null && other.date != null && (this.date - other.date).absoluteValue <= 1.minutes)
}
