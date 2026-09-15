package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("responseStatus")
    val responseStatus: ResponseStatus,
    @SerialName("serviceTicketId")
    val serviceTicketId: String? = null,
) {
    @Serializable
    data class ResponseStatus(
        val type: String,
    )

}
