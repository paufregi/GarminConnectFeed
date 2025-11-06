package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.ProfileType
import javax.inject.Inject

class GetProfileTypes @Inject constructor() {
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