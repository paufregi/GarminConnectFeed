package paufregi.connectfeed.presentation.app.profiles.list

import paufregi.connectfeed.core.models.Profile

data class ProfilesState(
    val profiles: List<Profile> = emptyList(),
)