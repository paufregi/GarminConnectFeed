package paufregi.connectfeed.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: Long,
    val name: String,
    val distance: Double,
    val type: ActivityType
)
