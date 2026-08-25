package paufregi.connectfeed.core.models

data class Gear(
    val id: String,
    val name: String,
    val type: GearType = GearType.Unknown,
    val distance: Long? = null,
)