package paufregi.connectfeed.presentation.gears

import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

data class GearsState(
    val process: ProcessState = ProcessState.Idle,
    val hasStrava: Boolean? = null,
    val gears: List<Profile> = emptyList(),
    val stravaGears: List<Profile> = emptyList(),
)
