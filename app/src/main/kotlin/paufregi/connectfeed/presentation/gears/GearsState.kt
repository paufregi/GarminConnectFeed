package paufregi.connectfeed.presentation.gears

import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.presentation.ui.models.ProcessState

data class GearsState(
    val gears: List<Gear> = emptyList(),
    val process: ProcessState = ProcessState.Idle,
)

