package paufregi.connectfeed.data.api.strava.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.strava.converters.InstantSerializer
import kotlin.time.Clock
import kotlin.time.Instant

@Serializable
data class AuthToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_at")
    @Serializable(with = InstantSerializer::class)
    val expiresAt: Instant,
    @SerialName("athlete")
    val athlete: SummaryAthlete,
) {
    fun isExpired(now: Instant = Clock.System.now()): Boolean =
        expiresAt < now

}