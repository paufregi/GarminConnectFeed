package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventType(
    @SerialName("typeId")
    val id: Long,
    @SerialName("typeKey")
    val key: String,
)