package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.Workout as CoreWorkout

@Serializable
data class Workout(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
) {
    fun toCore(): CoreWorkout = CoreWorkout(
        id = id,
        name = name,
    )
}