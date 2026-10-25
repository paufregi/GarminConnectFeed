package paufregi.connectfeed.presentation.app.profiles.list

import paufregi.connectfeed.core.models.Profile

sealed interface ProfilesAction {
    data class Delete(val profile: Profile): ProfilesAction
}
