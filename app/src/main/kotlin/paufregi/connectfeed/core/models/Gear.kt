package paufregi.connectfeed.core.models

data class Gear(
    val id: String,
    val name: String,
    val type: GearType? = null,
    val distance: Long? = null,
)