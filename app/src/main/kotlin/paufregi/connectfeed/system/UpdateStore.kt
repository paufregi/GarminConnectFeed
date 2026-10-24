package paufregi.connectfeed.system

import android.content.Context
import androidx.core.content.edit

/**
 * Remembers the download enqueued by this app so [Installer] only reacts to
 * `DOWNLOAD_COMPLETE` broadcasts for downloads it actually requested.
 */
class UpdateStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    val pendingDownloadId: Long?
        get() = prefs.getLong(KEY_DOWNLOAD_ID, NONE).takeIf { it != NONE }

    fun savePendingDownload(id: Long) = prefs.edit { putLong(KEY_DOWNLOAD_ID, id) }

    fun clearPendingDownload() = prefs.edit { remove(KEY_DOWNLOAD_ID) }

    private companion object {
        const val PREFS_NAME = "update"
        const val KEY_DOWNLOAD_ID = "download_id"
        const val NONE = -1L
    }
}

