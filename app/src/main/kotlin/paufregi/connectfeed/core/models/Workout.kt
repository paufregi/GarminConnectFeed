package paufregi.connectfeed.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: Long,
    val name: String,
)
