package paufregi.connectfeed.data.repository

import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.core.utils.mapOrFailure
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.github.Github
import javax.inject.Inject

class GithubRepository@Inject constructor(private val github: Github) {
suspend fun getLatestRelease() =
    github.getLatestRelease().toResult().mapOrFailure { r ->
        val version = Version.parse(r.tagName)
        val url = r.assets.find {
            it.contentType == "application/vnd.android.package-archive" &&
                it.downloadUrl.endsWith(".apk")
        }?.downloadUrl

        if (version != null && url != null) return@mapOrFailure Release(version, url)
    }
}