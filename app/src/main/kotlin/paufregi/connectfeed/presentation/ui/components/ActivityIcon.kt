package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.ui.icons.Breathwork
import paufregi.connectfeed.presentation.ui.icons.Connect
import paufregi.connectfeed.presentation.ui.icons.Cycling
import paufregi.connectfeed.presentation.ui.icons.EBiking
import paufregi.connectfeed.presentation.ui.icons.Fitness
import paufregi.connectfeed.presentation.ui.icons.Hiking
import paufregi.connectfeed.presentation.ui.icons.IndoorCycling
import paufregi.connectfeed.presentation.ui.icons.JumpRope
import paufregi.connectfeed.presentation.ui.icons.Meditation
import paufregi.connectfeed.presentation.ui.icons.OpenWaterSwimming
import paufregi.connectfeed.presentation.ui.icons.Other
import paufregi.connectfeed.presentation.ui.icons.Rugby
import paufregi.connectfeed.presentation.ui.icons.Running
import paufregi.connectfeed.presentation.ui.icons.Soccer
import paufregi.connectfeed.presentation.ui.icons.StrengthTraining
import paufregi.connectfeed.presentation.ui.icons.Swimming
import paufregi.connectfeed.presentation.ui.icons.TrailRunning
import paufregi.connectfeed.presentation.ui.icons.TreadmillRunning
import paufregi.connectfeed.presentation.ui.icons.Walking
import paufregi.connectfeed.presentation.ui.icons.Yoga


@Composable
@ExperimentalMaterial3Api
fun ActivityIcon(
    activityType: ActivityType?
) {

    val image = when (activityType) {
        is ActivityType.Any -> Icons.Connect.Other
        is ActivityType.Running -> Icons.Connect.Running
        is ActivityType.TrailRunning -> Icons.Connect.TrailRunning
        is ActivityType.TreadmillRunning -> Icons.Connect.TreadmillRunning
        is ActivityType.Cycling -> Icons.Connect.Cycling
        is ActivityType.IndoorCycling -> Icons.Connect.IndoorCycling
        is ActivityType.EBiking -> Icons.Connect.EBiking
        is ActivityType.Swimming -> Icons.Connect.Swimming
        is ActivityType.OpenWaterSwimming -> Icons.Connect.OpenWaterSwimming
        is ActivityType.Strength -> Icons.Connect.StrengthTraining
        is ActivityType.Fitness -> Icons.Connect.Fitness
        is ActivityType.Walking -> Icons.Connect.Walking
        is ActivityType.Hiking -> Icons.Connect.Hiking
        is ActivityType.Rugby -> Icons.Connect.Rugby
        is ActivityType.Soccer -> Icons.Connect.Soccer
        is ActivityType.JumpRope -> Icons.Connect.JumpRope
        is ActivityType.Breathwork -> Icons.Connect.Breathwork
        is ActivityType.Meditation -> Icons.Connect.Meditation
        is ActivityType.Yoga -> Icons.Connect.Yoga
        is ActivityType.Other -> Icons.Connect.Other
        else -> null
    }
    image?.let { Icon(it, it.name, Modifier.size(24.dp)) }
}
