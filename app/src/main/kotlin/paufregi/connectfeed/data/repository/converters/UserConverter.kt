package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.models.UserProfileDto

fun UserProfileDto.toCore() = User(
    id = id,
    name = name,
    profileImageUrl = avatarUrl
)