package paufregi.connectfeed.presentation.ui.components.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.components.frame.BottomApp
import paufregi.connectfeed.presentation.ui.components.frame.FormFrame
import paufregi.connectfeed.presentation.ui.components.frame.TopButton
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Strava
import paufregi.connectfeed.presentation.ui.utils.add
import paufregi.connectfeed.presentation.ui.utils.launchGarmin
import paufregi.connectfeed.presentation.ui.utils.launchStrava

@Composable
@ExperimentalMaterial3Api
internal fun EditSuccess(
    title: String,
    activity: Activity,
    navBack: () -> Unit = {},
    actionIcon: @Composable RowScope.() -> Unit = {},
) {
    val context = LocalContext.current

    FormFrame(
        title = title,
        navIcon = TopButton.back(enabled = false),
        actionIcon = actionIcon,
        bottomContent = BottomApp.ok(navBack)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(20.dp))
                .testTag("edit_success")
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                tint = Color.Green,
                modifier = Modifier
                    .size(110.dp)
                    .testTag("icon")
            )
            Text(
                text = "Update completed!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .testTag("title")
            )
            Button(
                text = "Open Garmin Connect",
                icon = Icons.Connect.Logo,
                onClick = { launchGarmin(context, activity.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("garmin")
            )
            activity.stravaId?.let {
                Button(
                    text = "Open Strava",
                    icon = Icons.Strava.Logo,
                    onClick = { launchStrava(context, activity.stravaId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("strava")
                )
            }
        }
    }
}