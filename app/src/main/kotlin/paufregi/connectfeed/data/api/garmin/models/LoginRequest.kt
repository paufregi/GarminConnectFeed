package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean = true,
    val captchaToken: String = "",
)
