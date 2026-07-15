package paufregi.connectfeed.core.models

data class Gear(
    val id: String,
    val name: String,
    val gearType: GearType? = null,
    val distance: Long? = null,
)