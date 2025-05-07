package paufregi.connectfeed.data.api.garmin.models

import com.auth0.jwt.JWT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import java.util.Date

@Serializable
@JsonIgnoreUnknownKeys
data class AuthToken(
    @SerialName("access_token")
    val accessToken: String,
) {
    fun isExpired(date: Date = Date()): Boolean {
        return accessToken.isBlank() || JWT.decode(accessToken).expiresAt.before(date)
    }
}


