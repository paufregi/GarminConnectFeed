package paufregi.connectfeed.core.models

data class StravaActivity(
    val id: Long,
    val name: String,
    val distance: Double?,
    val externalId: String,
    val type: ActivityType,
)