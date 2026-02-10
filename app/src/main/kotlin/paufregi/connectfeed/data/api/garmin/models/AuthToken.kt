package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.garmin.converters.AuthTokenSerializer
import kotlin.time.Clock
import kotlin.time.Instant

@Serializable(with = AuthTokenSerializer::class)
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Instant,
    val refreshExpiresAt: Instant
) {
    fun isExpired(now: Instant = Clock.System.now()): Boolean =
        expiresAt < now

    fun isRefreshExpired(now: Instant = Clock.System.now()): Boolean =
        refreshExpiresAt < now
}
