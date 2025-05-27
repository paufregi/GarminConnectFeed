package paufregi.connectfeed.core.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
)
