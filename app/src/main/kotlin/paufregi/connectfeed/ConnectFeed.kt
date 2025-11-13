package paufregi.connectfeed

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import paufregi.connectfeed.system.CleanUp

@HiltAndroidApp
class ConnectFeed : Application() {
    override fun onCreate() {
        super.onCreate()
        CleanUp(applicationContext).run()
    }
}