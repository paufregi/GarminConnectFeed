package paufregi.connectfeed.data.api.github.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseDto(
    @SerialName("tag_name")
    val tagName: String,
    @SerialName("assets")
    val assets: List<Asset>,
)