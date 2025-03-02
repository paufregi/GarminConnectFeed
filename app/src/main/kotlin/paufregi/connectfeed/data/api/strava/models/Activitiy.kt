package paufregi.connectfeed.data.api.strava.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Activity(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("sport_type")
    val sportType: String,
    @SerializedName("distance")
    val distance: Double,
)