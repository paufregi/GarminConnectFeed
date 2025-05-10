package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import paufregi.connectfeed.data.api.strava.converters.InstantSerializer
import java.time.Instant

@Serializable
@JsonIgnoreUnknownKeys
data class AuthToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_at")
    @Serializable(with = InstantSerializer::class)
    val expiresAt: Instant,
) {
    fun isExpired(now: Instant = Instant.now()): Boolean =
        expiresAt.isBefore(now)
}