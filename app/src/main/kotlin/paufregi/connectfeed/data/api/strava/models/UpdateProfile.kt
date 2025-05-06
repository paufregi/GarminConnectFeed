package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfile(
    @SerialName("weight")
    val weight: Float,
)