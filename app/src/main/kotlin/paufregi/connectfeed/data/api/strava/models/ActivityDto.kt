package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ActivityDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("sport_type")
    val sportType: String,
    @SerialName("distance")
    val distance: Double,
    @SerialName("start_date")
    val startDate: String?,
)