package paufregi.connectfeed.data.api.garmin.models

import com.appstractive.jwt.JWT
import com.appstractive.jwt.expiresAt
import com.appstractive.jwt.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

@Serializable
data class AuthToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
) {
    fun isExpired(now: Instant = Clock.System.now()): Boolean =
        JWT.from(accessToken).expiresAt?.let { now > it } ?: true
}
