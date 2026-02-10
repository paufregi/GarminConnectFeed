package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.strava.converters.SportTypeConverter
import kotlin.math.round
import kotlin.time.Instant
import paufregi.connectfeed.core.models.Activity as CoreActivity

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
    val startDate: String?,
) {
    fun toCore(): CoreActivity = CoreActivity(
        id = id,
        name = name,
        distance = round(this.distance),
        type = SportTypeConverter.toActivityType(sportType),
        date = this.startDate?.let { Instant.parse(it) }
    )
}