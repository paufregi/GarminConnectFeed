package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.strava.serializers.LocalDateTimeSerializer
import paufregi.connectfeed.data.api.strava.serializers.SportTypeSerializer
import java.time.LocalDateTime

@Serializable
data class Activity(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("sport_type")
    @Serializable(with = SportTypeSerializer::class)
    val sportType: SportType,
    @SerialName("distance")
    val distance: Double,
    @SerialName("start_date_local")
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDateTime: LocalDateTime,
)