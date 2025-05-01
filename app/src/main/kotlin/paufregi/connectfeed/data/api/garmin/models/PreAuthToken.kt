package paufregi.connectfeed.data.api.garmin.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PreAuthToken(
    val token: String,
    val secret: String,
) {
    fun isValid(): Boolean {
        return token.isNotBlank() && secret.isNotBlank()
    }
}


