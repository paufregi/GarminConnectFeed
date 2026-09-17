package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.strava.serializer.SportTypeSerializer
import paufregi.connectfeed.data.api.strava.serializer.TimestampSerializer
import kotlin.time.Instant

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
    @SerialName("start_date")
    @Serializable(with = TimestampSerializer::class)
    val startDate: Instant,
)