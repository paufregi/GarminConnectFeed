package paufregi.connectfeed.system

import android.content.Context
import android.util.Log

class CleanUp(val context: Context) {
    fun run() {
        val updateDir = context.getExternalFilesDir("update")
        if (updateDir?.exists() == true && updateDir.isDirectory) {
            Log.i("CleanUp", "Cleaning up `update` folder")
            updateDir.listFiles()?.forEach {
                Log.i("CleanUp", "Deleting $it")
                it.delete()
            }
        }
    }
}