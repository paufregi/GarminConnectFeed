package paufregi.connectfeed.presentation.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import paufregi.connectfeed.core.models.Activity

fun launchStrava(context: Context, activity: Activity?) {
    activity?.let {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, "https://www.strava.com/activities/${it.id}".toUri())
        )
    }
}