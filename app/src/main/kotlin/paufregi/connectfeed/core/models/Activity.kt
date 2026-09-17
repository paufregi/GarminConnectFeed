package paufregi.connectfeed.core.models

import kotlin.time.Instant

data class Activity(
    val id: Long,
    val name: String,
    val type: ActivityType,
    val eventType: EventType,
    val distance: Double? = null,
    val trainingEffect: String? = null,
    val date: Instant,
    val workoutId: Long? = null,
    val stravaId: Long? = null,
)
