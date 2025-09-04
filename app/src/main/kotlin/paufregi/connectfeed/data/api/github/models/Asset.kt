package paufregi.connectfeed.data.api.github.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Asset(
    @SerialName("content_type")
    val contentType: String,
    @SerialName("browser_download_url")
    val downloadUrl: String,
)