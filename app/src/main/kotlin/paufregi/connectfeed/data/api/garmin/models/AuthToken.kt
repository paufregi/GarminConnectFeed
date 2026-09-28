package paufregi.connectfeed.data.api.garmin.models

import android.util.Log
import com.appstractive.jwt.JWT
import com.appstractive.jwt.from
import com.appstractive.jwt.issuedAt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Serializable
data class AuthToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("refresh_token_expires_in")
    val refreshTokenExpiresIn: Int,
) {
    val issuedAt: Instant
        get() = JWT.from(accessToken).issuedAt ?: Clock.System.now()

    fun isExpired(now: Instant = Clock.System.now()): Boolean {
        Log.i("AuthToken", "Checking if token is expired: issuedAt=$issuedAt, expiresIn=$expiresIn, now=$now")
        Log.i("AuthToken", "Token expires at: ${issuedAt + expiresIn.seconds}")
        Log.i("AuthToken", "Token is expired: ${issuedAt + expiresIn.seconds <= now}")
        return issuedAt + expiresIn.seconds <= now
    }


    fun isRefreshTokenExpired(now: Instant = Clock.System.now()): Boolean =
        issuedAt + refreshTokenExpiresIn.seconds > now
}
