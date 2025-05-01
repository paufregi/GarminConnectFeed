package paufregi.connectfeed.data.api.garmin.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.Instant

@Keep
@Serializable
data class AuthToken(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_at")
    val expiresAt: Long,
) {
    fun isExpired(date: Long = Instant.now().epochSecond): Boolean = expiresAt < date
}


