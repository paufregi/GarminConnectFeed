package paufregi.connectfeed.core.models

import java.time.LocalDateTime

data class Activity(
    val id: Long,
    val name: String,
    val type: ActivityType,
    val eventType: EventType,
    val distance: Double? = null,
    val trainingEffect: String? = null,
    val date: LocalDateTime,
    val workoutId: Long? = null,
    val stravaId: Long? = null,
)
