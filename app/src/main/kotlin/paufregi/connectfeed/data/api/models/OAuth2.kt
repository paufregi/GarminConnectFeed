package paufregi.connectfeed.data.api.models

import androidx.annotation.Keep
import com.auth0.jwt.JWT
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.Date

@Keep
@Serializable
data class OAuth2(
    @SerializedName("scope")
    val scope: String,
    @SerializedName("jti")
    val jti: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("refresh_token_expires_in")
    val refreshTokenExpiresIn: Long,
) {
    fun isExpired(date: Date = Date()): Boolean {
        return accessToken.isBlank() || JWT.decode(accessToken).expiresAt.before(date)
    }
}


