package paufregi.connectfeed.presentation.app.profiles.list

import paufregi.connectfeed.core.models.Profile

data class ProfilesState(
    val isLoading: Boolean = true,
    val profiles: List<Profile> = emptyList(),
)