package paufregi.connectfeed.presentation.syncweight

import paufregi.connectfeed.presentation.ui.models.ProcessState

data class SyncWeightState(
    val process: ProcessState = ProcessState.Idle
)
