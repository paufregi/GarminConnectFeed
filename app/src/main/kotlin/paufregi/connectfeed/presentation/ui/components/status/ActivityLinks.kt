package paufregi.connectfeed.presentation.ui.components.status

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Strava
import paufregi.connectfeed.presentation.ui.utils.launchGarmin
import paufregi.connectfeed.presentation.ui.utils.launchStrava

/** Deep-link buttons to open [activity] in Garmin Connect and, when linked, Strava. */
@Composable
fun ActivityLinks(activity: Activity) {
    val context = LocalContext.current

    Button(
        text = "Open Garmin Connect",
        icon = Icons.Connect.Logo,
        onClick = { launchGarmin(context, activity.id) },
        modifier = Modifier.fillMaxWidth().testTag("garmin")
    )
    activity.stravaId?.let { stravaId ->
        Button(
            text = "Open Strava",
            icon = Icons.Strava.Logo,
            onClick = { launchStrava(context, stravaId) },
            modifier = Modifier.fillMaxWidth().testTag("strava")
        )
    }
}

