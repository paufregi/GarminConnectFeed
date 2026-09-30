package paufregi.connectfeed.presentation.app.gears

import paufregi.connectfeed.core.models.Gear

data class GearsState(
    val loading: Boolean = false,
    val gears: List<Gear> = emptyList(),
)

