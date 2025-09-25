package paufregi.connectfeed.data.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import paufregi.connectfeed.core.models.Release
import java.io.File

data class Download(val id: Long, val progress: Long, val uri: String?)

class Updater(private val context: Context) {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadApk(release: Release): Result<Flow<Download>> {
        if (!release.downloadUrl.endsWith(".apk")) {
            return Result.failure(IllegalArgumentException("Invalid APK URL"))
        }

        val apkName = release.downloadUrl.substringAfterLast("/").substringBefore("?")
        val request = DownloadManager.Request(release.downloadUrl.toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            .setTitle(apkName)
            .setDestinationInExternalFilesDir(context, "update", apkName)

        val downloadId = downloadManager.enqueue(request)

        return Result.success(observeProgress(downloadId))
    }

    fun installApk(apk: String): Unit {
        val file = File(apk.removePrefix("file://"))
        val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)
    }

    private fun observeProgress(id: Long): Flow<Download> = flow {
        var downloading = true
        var uri: String? = ""
        while (downloading) {
            withContext(Dispatchers.IO) {
                val query = DownloadManager.Query().setFilterById(id)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val bytesDownloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val bytesTotal = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    uri = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
                    if (bytesTotal > 0) {
                        emit(Download(id, bytesDownloaded * 100L / bytesTotal, uri))
                    }
                    downloading = status == DownloadManager.STATUS_RUNNING || status == DownloadManager.STATUS_PENDING
                }
                cursor.close()
            }
            delay(100)
        }
        emit(Download(id, 100, uri))
    }

}