package paufregi.connectfeed.presentation.profiles

import paufregi.connectfeed.core.models.Profile

sealed interface ProfileAction {
    data class Delete(val profile: Profile) : ProfileAction
}
