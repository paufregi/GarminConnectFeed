package paufregi.connectfeed.system

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

class Installer : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent?.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return

        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        val store = UpdateStore(context)
        // Ignore broadcasts for downloads this app did not request.
        if (id == -1L || id != store.pendingDownloadId) return

        store.clearPendingDownload()
        val downloadManager = context.getSystemService(DownloadManager::class.java) ?: return
        val apk = downloadedApk(downloadManager, id) ?: return

        val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", apk)
        val install = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, APK_MIME_TYPE)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(install)
    }

    private fun downloadedApk(downloadManager: DownloadManager, id: Long): File? =
        downloadManager.query(DownloadManager.Query().setFilterById(id)).use { cursor ->
            if (!cursor.moveToFirst()) return null

            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            if (status != DownloadManager.STATUS_SUCCESSFUL) return null

            val uri: String? = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
            uri?.let { File(it.removePrefix("file://")) }
        }

    private companion object {
        const val APK_MIME_TYPE = "application/vnd.android.package-archive"
    }
}