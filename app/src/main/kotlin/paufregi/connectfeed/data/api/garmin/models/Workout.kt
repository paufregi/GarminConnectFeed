package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    @SerialName("workoutId")
    val id: Long,
    @SerialName("workoutName")
    val name: String,
)