package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    @SerialName("waterConsumed")
    val water: Int?,
    @SerialName("directWorkoutFeel")
    val feel: Float?, // 0.0-25.0-50.0-75.0-100.0
    @SerialName("directWorkoutRpe")
    val effort: Float?, // 10.0-100.0 (step 10.0)
)