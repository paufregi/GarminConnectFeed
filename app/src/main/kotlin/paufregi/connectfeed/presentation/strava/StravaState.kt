package paufregi.connectfeed.presentation.strava

import paufregi.connectfeed.presentation.ui.models.ProcessState

data class StravaState(
    val process: ProcessState = ProcessState.Idle
)