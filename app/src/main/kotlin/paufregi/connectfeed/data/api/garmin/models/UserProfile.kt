package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import paufregi.connectfeed.core.models.User as CoreUserProfile

@Serializable
@JsonIgnoreUnknownKeys
data class UserProfile(
    @SerialName("fullName")
    val name: String,

    @SerialName("profileImageUrlLarge")
    val avatarUrl: String,
) {
    fun toCore(): CoreUserProfile = CoreUserProfile(
        name = name,
        profileImageUrl = avatarUrl
    )
}