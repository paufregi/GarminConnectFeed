package paufregi.connectfeed.system

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.utils.failure
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

enum class DownloadStatus { Running, Completed, Failed }

class Downloader(
    private val context: Context,
    private val downloadManager: DownloadManager,
) {

    fun downloadApk(release: Release): Result<Long> {
        if (!release.downloadUrl.endsWith(".apk")) {
            return Result.failure(IllegalArgumentException("Invalid APK URL"))
        }

        val apkName = release.downloadUrl.substringAfterLast("/").substringBefore("?")
        val request = DownloadManager.Request(release.downloadUrl.toUri())
            .setMimeType(APK_MIME_TYPE)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setTitle(apkName)
            .setDestinationInExternalFilesDir(context, UPDATE_DIR, apkName)

        return runCatching { downloadManager.enqueue(request) }
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

    /**
     * Launches the system installer for a completed download. Must be called while the app is in
     * the foreground: background activity starts are blocked, so this cannot run from a receiver.
     */
    fun install(id: Long): Result<Unit> {
        val apk = downloadedApk(id) ?: return Result.failure("Downloaded update not found")

        val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", apk)
        val install = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, APK_MIME_TYPE)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        return runCatching { context.startActivity(install) }
    }

    private fun queryStatus(id: Long): DownloadStatus? =
        downloadManager.query(DownloadManager.Query().setFilterById(id)).use { cursor ->
            if (!cursor.moveToFirst()) return null

            when (cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.Completed
                DownloadManager.STATUS_FAILED -> DownloadStatus.Failed
                else -> DownloadStatus.Running
            }
        }

    private fun downloadedApk(id: Long): File? =
        downloadManager.query(DownloadManager.Query().setFilterById(id)).use { cursor ->
            if (!cursor.moveToFirst()) return null

            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            if (status != DownloadManager.STATUS_SUCCESSFUL) return null

            val uri: String? = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
            uri?.let { File(it.removePrefix("file://")) }
        }

    companion object {
        const val UPDATE_DIR = "update"
        private const val APK_MIME_TYPE = "application/vnd.android.package-archive"
        private val POLL_INTERVAL_MS = 250L.milliseconds
    }
}

