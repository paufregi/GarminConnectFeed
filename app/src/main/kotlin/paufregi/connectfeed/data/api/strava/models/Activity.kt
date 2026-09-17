package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.utils.serializers.TimestampSerializer
import kotlin.time.Instant

@Serializable
data class Activity(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("sport_type")
    val sportType: String,
    @SerialName("distance")
    val distance: Double,
    @SerialName("start_date")
    @Serializable(with = TimestampSerializer::class)
    val startDate: Instant,
)