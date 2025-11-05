package paufregi.connectfeed.presentation.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.ProfileType
import paufregi.connectfeed.presentation.ui.icons.Activities
import paufregi.connectfeed.presentation.ui.icons.Breathwork
import paufregi.connectfeed.presentation.ui.icons.Connect
import paufregi.connectfeed.presentation.ui.icons.Cycling
import paufregi.connectfeed.presentation.ui.icons.EBiking
import paufregi.connectfeed.presentation.ui.icons.Fitness
import paufregi.connectfeed.presentation.ui.icons.Hiking
import paufregi.connectfeed.presentation.ui.icons.JumpRope
import paufregi.connectfeed.presentation.ui.icons.OpenWaterSwimming
import paufregi.connectfeed.presentation.ui.icons.Running
import paufregi.connectfeed.presentation.ui.icons.StrengthTraining
import paufregi.connectfeed.presentation.ui.icons.Swimming
import paufregi.connectfeed.presentation.ui.icons.TrailRunning
import paufregi.connectfeed.presentation.ui.icons.TreadmillRunning
import paufregi.connectfeed.presentation.ui.icons.Walking
import paufregi.connectfeed.presentation.ui.icons.Yoga

fun iconForActivityType(type: ActivityType): ImageVector? =
    when (type) {
        is ActivityType.Running -> Icons.Connect.Running
        is ActivityType.TrailRunning -> Icons.Connect.TrailRunning
        is ActivityType.TreadmillRunning -> Icons.Connect.TreadmillRunning
        is ActivityType.Cycling -> Icons.Connect.Cycling
        is ActivityType.EBiking -> Icons.Connect.EBiking
        is ActivityType.Swimming -> Icons.Connect.Swimming
        is ActivityType.OpenWaterSwimming -> Icons.Connect.OpenWaterSwimming
        is ActivityType.Walking -> Icons.Connect.Walking
        is ActivityType.Hiking -> Icons.Connect.Hiking
        is ActivityType.JumpRope -> Icons.Connect.JumpRope
        is ActivityType.Breathwork -> Icons.Connect.Breathwork
        is ActivityType.Yoga -> Icons.Connect.Yoga
        else -> null
    }

fun iconForProfileType(type: ProfileType): ImageVector? =
    when(type) {
        is ProfileType.Any -> Icons.Connect.Activities
        is ProfileType.Running -> Icons.Connect.Running
        is ProfileType.Cycling -> Icons.Connect.Cycling
        is ProfileType.Swimming -> Icons.Connect.Swimming
        is ProfileType.Strength -> Icons.Connect.StrengthTraining
        is ProfileType.Fitness -> Icons.Connect.Fitness
        else -> Icons.Connect.Activities
    }