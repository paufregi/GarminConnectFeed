package paufregi.connectfeed.data.api.garmin.models

import androidx.annotation.Keep
import com.auth0.jwt.JWT
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.Date

@Keep
@Serializable
data class OAuth2(
    @SerializedName("access_token")
    val accessToken: String,
) {
    fun isExpired(date: Date = Date()): Boolean {
        return accessToken.isBlank() || JWT.decode(accessToken).expiresAt.before(date)
    }
}


