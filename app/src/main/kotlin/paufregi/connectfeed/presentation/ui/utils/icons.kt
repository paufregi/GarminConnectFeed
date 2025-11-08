package paufregi.connectfeed.presentation.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.ui.icons.garmin.Activity
import paufregi.connectfeed.presentation.ui.icons.garmin.Breathwork
import paufregi.connectfeed.presentation.ui.icons.garmin.Cardio
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Cycling
import paufregi.connectfeed.presentation.ui.icons.garmin.DownhillBiking
import paufregi.connectfeed.presentation.ui.icons.garmin.EBiking
import paufregi.connectfeed.presentation.ui.icons.garmin.EBikingMountain
import paufregi.connectfeed.presentation.ui.icons.garmin.GravelCycling
import paufregi.connectfeed.presentation.ui.icons.garmin.HIIT
import paufregi.connectfeed.presentation.ui.icons.garmin.Hiking
import paufregi.connectfeed.presentation.ui.icons.garmin.JumpRope
import paufregi.connectfeed.presentation.ui.icons.garmin.Kayaking
import paufregi.connectfeed.presentation.ui.icons.garmin.MountainBiking
import paufregi.connectfeed.presentation.ui.icons.garmin.OpenWaterSwimming
import paufregi.connectfeed.presentation.ui.icons.garmin.RoadBiking
import paufregi.connectfeed.presentation.ui.icons.garmin.Running
import paufregi.connectfeed.presentation.ui.icons.garmin.Snowboarding
import paufregi.connectfeed.presentation.ui.icons.garmin.StandUpPaddling
import paufregi.connectfeed.presentation.ui.icons.garmin.StrengthTraining
import paufregi.connectfeed.presentation.ui.icons.garmin.Surfing
import paufregi.connectfeed.presentation.ui.icons.garmin.Swimming
import paufregi.connectfeed.presentation.ui.icons.garmin.TrackRunning
import paufregi.connectfeed.presentation.ui.icons.garmin.TrailRunning
import paufregi.connectfeed.presentation.ui.icons.garmin.TreadmillRunning
import paufregi.connectfeed.presentation.ui.icons.garmin.UltraRun
import paufregi.connectfeed.presentation.ui.icons.garmin.VirtualRide
import paufregi.connectfeed.presentation.ui.icons.garmin.Walking
import paufregi.connectfeed.presentation.ui.icons.garmin.Windsurf
import paufregi.connectfeed.presentation.ui.icons.garmin.Yoga

fun iconFor(type: ActivityType): ImageVector? =
    when (type) {
        is ActivityType.Any -> Icons.Connect.Activity
        is ActivityType.Other -> Icons.Connect.Activity
        is ActivityType.Fitness -> Icons.Connect.Cardio
        is ActivityType.Unknown -> Icons.Connect.Activity
        // GARMIN
        // Running
        is ActivityType.Running -> Icons.Connect.Running
        is ActivityType.TrackRunning -> Icons.Connect.TrackRunning
        is ActivityType.TrailRunning -> Icons.Connect.TrailRunning
        is ActivityType.TreadmillRunning -> Icons.Connect.TreadmillRunning
        is ActivityType.UltraRun -> Icons.Connect.UltraRun
        // Cycling
        is ActivityType.Cycling -> Icons.Connect.Cycling
        is ActivityType.DownhillBiking -> Icons.Connect.DownhillBiking
        is ActivityType.EBiking -> Icons.Connect.EBiking
        is ActivityType.EBikingMountain -> Icons.Connect.EBikingMountain
        is ActivityType.GravelCycling -> Icons.Connect.GravelCycling
        is ActivityType.MountainBiking -> Icons.Connect.MountainBiking
        is ActivityType.RoadBiking -> Icons.Connect.RoadBiking
        is ActivityType.VirtualRide -> Icons.Connect.VirtualRide
        // Fitness
        is ActivityType.HIIT -> Icons.Connect.HIIT
        is ActivityType.Breathwork -> Icons.Connect.Breathwork
        is ActivityType.Cardio -> Icons.Connect.Cardio
        is ActivityType.JumpRope -> Icons.Connect.JumpRope
        is ActivityType.StrengthTraining -> Icons.Connect.StrengthTraining
        is ActivityType.Yoga -> Icons.Connect.Yoga
        // Swimming
        is ActivityType.Swimming -> Icons.Connect.Swimming
        is ActivityType.PoolSwimming -> Icons.Connect.Swimming
        is ActivityType.OpenWaterSwimming -> Icons.Connect.OpenWaterSwimming
        // Other
        is ActivityType.Walking -> Icons.Connect.Walking
        is ActivityType.Hiking -> Icons.Connect.Hiking
        is ActivityType.Snowboarding -> Icons.Connect.Snowboarding
        is ActivityType.Kayaking -> Icons.Connect.Kayaking
        is ActivityType.StandUpPaddling -> Icons.Connect.StandUpPaddling
        is ActivityType.Surfing -> Icons.Connect.Surfing
        is ActivityType.Windsurf -> Icons.Connect.Windsurf
        else -> null
    }