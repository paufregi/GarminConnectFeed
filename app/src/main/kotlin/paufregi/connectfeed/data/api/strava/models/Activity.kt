package paufregi.connectfeed.data.api.strava.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.strava.converters.SportTypeConverter
import kotlin.math.round
import paufregi.connectfeed.core.models.Activity as CoreActivity

@Keep
@Serializable
data class Activity(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("sport_type")
    val sportType: String,
    @SerializedName("distance")
    val distance: Double,
) {
    fun toCore(): CoreActivity = CoreActivity(
        id = id,
        name = name,
        distance = round(this.distance),
        type = SportTypeConverter.toActivityType(sportType),
    )
}