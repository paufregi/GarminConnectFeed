package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAthleteDto(
    @SerialName("weight")
    val weight: Float,
)