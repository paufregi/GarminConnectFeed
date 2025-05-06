package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import java.time.Instant

@Serializable
@JsonIgnoreUnknownKeys
data class AuthToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_at")
    val expiresAt: Long,
) {
    fun isExpired(date: Long = Instant.now().epochSecond): Boolean = expiresAt < date
}


