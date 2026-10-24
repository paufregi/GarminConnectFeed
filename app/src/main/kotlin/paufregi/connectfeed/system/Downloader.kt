package paufregi.connectfeed.system

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import paufregi.connectfeed.core.models.Release
import kotlin.time.Duration.Companion.milliseconds

enum class DownloadStatus { Running, Completed, Failed }

class Downloader(
    private val context: Context,
    private val downloadManager: DownloadManager,
    private val store: UpdateStore,
) {

    fun downloadApk(release: Release): Result<Long> {
        if (!release.downloadUrl.endsWith(".apk")) {
            return Result.failure(IllegalArgumentException("Invalid APK URL"))
        }

        val apkName = release.downloadUrl.substringAfterLast("/").substringBefore("?")
        val request = DownloadManager.Request(release.downloadUrl.toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setTitle(apkName)
            .setDestinationInExternalFilesDir(context, UPDATE_DIR, apkName)

        return runCatching { downloadManager.enqueue(request) }
            .onSuccess { store.savePendingDownload(it) }
    }

    /** Polls the download until it reaches a terminal status; a removed download is reported as failed. */
    fun status(id: Long): Flow<DownloadStatus> = flow {
        while (true) {
            val status = queryStatus(id) ?: DownloadStatus.Failed
            emit(status)
            if (status != DownloadStatus.Running) return@flow
            delay(POLL_INTERVAL_MS)
        }
    }.flowOn(Dispatchers.IO)

    private fun queryStatus(id: Long): DownloadStatus? =
        downloadManager.query(DownloadManager.Query().setFilterById(id)).use { cursor ->
            if (!cursor.moveToFirst()) return null

            when (cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.Completed
                DownloadManager.STATUS_FAILED -> DownloadStatus.Failed
                else -> DownloadStatus.Running
            }
        }

    companion object {
        const val UPDATE_DIR = "update"
        private val POLL_INTERVAL_MS = 250L.milliseconds
    }
}