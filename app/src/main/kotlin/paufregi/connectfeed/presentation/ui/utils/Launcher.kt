package paufregi.connectfeed.presentation.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import paufregi.connectfeed.core.models.Activity

fun launchGarmin(context: Context, id: Long) {
    val appIntent = Intent(Intent.ACTION_VIEW, "garminconnect://activity/$id".toUri())
    val webIntent = Intent(Intent.ACTION_VIEW, "https://connect.garmin.com/modern/activity/$id".toUri())
    runCatching { context.startActivity(appIntent) }.recoverCatching { context.startActivity(webIntent) }
}

fun launchStrava(context: Context, id: Long) {
    val stravaIntent = Intent(Intent.ACTION_VIEW, "https://www.strava.com/activities/$id/edit".toUri())
    context.startActivity(stravaIntent)
}