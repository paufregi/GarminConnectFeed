package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.garmin.converters.AuthTokenSerializer
import java.time.Instant

@Serializable(with = AuthTokenSerializer::class)
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Instant,
    val refreshExpiresAt: Instant
) {
    fun isExpired(now: Instant = Instant.now()): Boolean =
        expiresAt.isBefore(now)

    fun isRefreshExpired(now: Instant = Instant.now()): Boolean =
        refreshExpiresAt.isBefore(now)
}
