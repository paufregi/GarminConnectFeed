package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.ui.icons.Breathwork
import paufregi.connectfeed.presentation.ui.icons.Connect
import paufregi.connectfeed.presentation.ui.icons.Cycling
import paufregi.connectfeed.presentation.ui.icons.EBiking
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

@Preview
@Composable
@ExperimentalMaterial3Api
fun ActivityIcon(
    @PreviewParameter(ActivityTypePreview::class) activityType: ActivityType?
) {
    when (activityType) {
        is ActivityType.Any -> Icon(Icons.Connect.Other, "any", Modifier.size(24.dp))
        is ActivityType.Running -> Icon(Icons.Connect.Running, "running", Modifier.size(24.dp))
        is ActivityType.TrailRunning -> Icon(
            Icons.Connect.TrailRunning,
            "trail_running",
            Modifier.size(24.dp)
        )

        is ActivityType.TreadmillRunning -> Icon(
            Icons.Connect.TreadmillRunning,
            "treadmill_running",
            Modifier.size(24.dp)
        )

        is ActivityType.Cycling -> Icon(Icons.Connect.Cycling, "cycling", Modifier.size(24.dp))
        is ActivityType.IndoorCycling -> Icon(
            Icons.Connect.IndoorCycling,
            "indoor_cycling",
            Modifier.size(24.dp)
        )

        is ActivityType.EBiking -> Icon(Icons.Connect.EBiking, "eBiking", Modifier.size(24.dp))
        is ActivityType.Swimming -> Icon(Icons.Connect.Swimming, "swimming", Modifier.size(24.dp))
        is ActivityType.OpenWaterSwimming -> Icon(
            Icons.Connect.OpenWaterSwimming,
            "open_water_swimming",
            Modifier.size(24.dp)
        )

        is ActivityType.Strength -> Icon(
            Icons.Connect.StrengthTraining,
            "strength_training",
            Modifier.size(24.dp)
        )

        is ActivityType.Walking -> Icon(Icons.Connect.Walking, "walking", Modifier.size(24.dp))
        is ActivityType.Hiking -> Icon(Icons.Connect.Hiking, "hiking", Modifier.size(24.dp))
        is ActivityType.Rugby -> Icon(Icons.Connect.Rugby, "rugby", Modifier.size(24.dp))
        is ActivityType.Soccer -> Icon(Icons.Connect.Soccer, "soccer", Modifier.size(24.dp))
        is ActivityType.JumpRope -> Icon(Icons.Connect.JumpRope, "jump_rope", Modifier.size(24.dp))
        is ActivityType.Breathwork -> Icon(
            Icons.Connect.Breathwork,
            "breathwork",
            Modifier.size(24.dp)
        )

        is ActivityType.Meditation -> Icon(
            Icons.Connect.Meditation,
            "meditation",
            Modifier.size(24.dp)
        )

        is ActivityType.Yoga -> Icon(Icons.Connect.Yoga, "yoga", Modifier.size(24.dp))
        is ActivityType.Other -> Icon(Icons.Connect.Other, "other", Modifier.size(24.dp))
        else -> null
    }
}

private class ActivityTypePreview : PreviewParameterProvider<ActivityType?> {
    override val values = sequenceOf(
        ActivityType.Running,
        ActivityType.Cycling,
        ActivityType.Strength,
        ActivityType.Any,
        ActivityType.Unknown,
        null
    )
}