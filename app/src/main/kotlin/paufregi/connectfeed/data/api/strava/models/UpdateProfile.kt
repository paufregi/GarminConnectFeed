package paufregi.connectfeed.data.api.strava.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UpdateProfile(
    @SerializedName("weight")
    val weight: Float,
)