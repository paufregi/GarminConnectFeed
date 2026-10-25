package paufregi.connectfeed.system

import android.content.Context

class CleanUp(val context: Context) {
    fun run() {
        val updateDir = context.getExternalFilesDir(Downloader.UPDATE_DIR)
        if (updateDir?.exists() == true && updateDir.isDirectory) {
            updateDir.listFiles()?.forEach { it.delete() }
        }
    }
}