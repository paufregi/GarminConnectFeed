package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.Serializable

@Serializable
data class PreAuthToken(
    val token: String,
    val secret: String,
) {
    fun isValid(): Boolean {
        return token.isNotBlank() && secret.isNotBlank()
    }
}


