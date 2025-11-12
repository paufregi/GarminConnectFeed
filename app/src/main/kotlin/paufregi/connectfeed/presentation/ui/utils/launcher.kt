package paufregi.connectfeed.presentation.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import paufregi.connectfeed.core.models.Activity

fun launchStrava(context: Context, activity: Activity?) {
    activity?.let {
        val stravaIntent = Intent(Intent.ACTION_VIEW, "https://www.strava.com/activities/${it.id}/edit".toUri())
        context.startActivity(stravaIntent)
    }
}

fun launchGarmin(context: Context, activity: Activity?) {
    activity?.let {
        val appIntent = Intent(Intent.ACTION_VIEW, "garminconnect://activity/${it.id}".toUri())
        val webIntent = Intent(Intent.ACTION_VIEW, "https://connect.garmin.com/modern/activity/${it.id}".toUri())
        runCatching { context.startActivity(appIntent) }.recover { context.startActivity(webIntent) }
    }
}