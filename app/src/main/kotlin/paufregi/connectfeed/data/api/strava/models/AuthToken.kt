package paufregi.connectfeed.data.api.strava.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.time.Instant

@Keep
data class AuthToken(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("expires_at")
    val expiresAt: Long,
) {
    fun isExpired(date: Long = Instant.now().epochSecond): Boolean = expiresAt < date
}