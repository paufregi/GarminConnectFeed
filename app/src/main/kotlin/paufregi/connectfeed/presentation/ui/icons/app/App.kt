package paufregi.connectfeed.presentation.ui.icons.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Shoe

object AppIcons {
    val Activities = Icons.Filled.Home
    val Profiles = Icons.Filled.Tune
    val Gears = Icons.Connect.Shoe
    val Settings = Icons.Filled.Settings

    val Edit = Icons.Filled.Edit
    val QuickEdit = Icons.Filled.EditNote
}

val Icons.App: AppIcons
    get() = AppIcons