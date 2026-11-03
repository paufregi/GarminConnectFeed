package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    @SerialName("id")
    val id: Long,
    @SerialName("fullName")
    val name: String,
    @SerialName("profileImageUrlLarge")
    val avatarUrl: String,
) {

}