package paufregi.connectfeed.presentation.app.gears

import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.presentation.ui.models.ProcState

data class GearsState(
    val process: ProcState? = null,
    val gears: List<Gear> = emptyList(),
)

