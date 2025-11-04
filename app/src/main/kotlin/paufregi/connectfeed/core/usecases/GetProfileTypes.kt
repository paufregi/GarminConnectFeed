package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.ProfileType

class GetProfileTypes {
    operator fun invoke(): List<ProfileType> = listOf(
        ProfileType.Any,
        ProfileType.Running,
        ProfileType.Cycling,
        ProfileType.Swimming,
        ProfileType.Strength,
        ProfileType.Fitness,
        ProfileType.Other
    ).sortedBy { it.order }
}