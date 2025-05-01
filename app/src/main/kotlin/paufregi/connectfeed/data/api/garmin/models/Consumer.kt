package paufregi.connectfeed.data.api.garmin.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Consumer(
    @SerializedName("consumer_key")
    val key: String,
    @SerializedName("consumer_secret")
    val secret: String
)
