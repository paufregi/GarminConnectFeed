package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.data.api.github.models.ReleaseDto

fun ReleaseDto.toCore(): Release? {
    val version = Version.parse(this.tagName)
    val downloadUrl = this.assets.find { it.contentType == "application/vnd.android.package-archive" && it.downloadUrl.endsWith(".apk") }?.downloadUrl

    return version?.let { v -> downloadUrl?.let { url -> Release(v, url) } }
}