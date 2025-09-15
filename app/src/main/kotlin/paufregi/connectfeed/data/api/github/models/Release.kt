package paufregi.connectfeed.data.api.github.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.core.models.Release as CoreRelease

@Serializable
data class Release(
    @SerialName("tag_name")
    val tagName: String,
    @SerialName("assets")
    val assets: List<Asset>,
) {
    fun toCore(): CoreRelease? {
        val version = Version.parse(this.tagName)
        val downloadUrl = this.assets.find { it.contentType == "application/vnd.android.package-archive" && it.downloadUrl.endsWith(".apk") }?.downloadUrl

        return version?.let { v -> downloadUrl?.let { url ->  CoreRelease(v, url) } }

    }
}