package paufregi.connectfeed.data.api.strava.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Token(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("expires_at")
    val expiresAt: Long,
) {
    fun isExpired(date: Long = System.currentTimeMillis()): Boolean {
        return expiresAt < date
    }
}