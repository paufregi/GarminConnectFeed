package paufregi.connectfeed.presentation.activities

import paufregi.connectfeed.presentation.ui.models.ProcessState

data class ActivitiesState(
    val process: ProcessState = ProcessState.Idle,
)

