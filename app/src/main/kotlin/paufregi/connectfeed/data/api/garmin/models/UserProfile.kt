package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.User as CoreUserProfile

@Serializable
data class UserProfile(
    @SerialName("id")
    val id: Long,
    @SerialName("fullName")
    val name: String,
    @SerialName("profileImageUrlLarge")
    val avatarUrl: String,
) {
    fun toCore(): CoreUserProfile = CoreUserProfile(
        id = id,
        name = name,
        profileImageUrl = avatarUrl
    )
}