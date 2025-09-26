package paufregi.connectfeed.system

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import paufregi.connectfeed.core.models.Release

class Downloader(private val context: Context) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadApk(release: Release): Result<Unit> {
        if (!release.downloadUrl.endsWith(".apk")) {
            return Result.failure(IllegalArgumentException("Invalid APK URL"))
        }

        val apkName = release.downloadUrl.substringAfterLast("/").substringBefore("?")
        val request = DownloadManager.Request(release.downloadUrl.toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setTitle(apkName)
            .setDestinationInExternalFilesDir(context, "update", apkName)

        return runCatching { downloadManager.enqueue(request) }
    }
}