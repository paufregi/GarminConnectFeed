package paufregi.connectfeed.presentation.app.activities.quickedit

import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Profile

sealed interface QuickEditAction {
    data class SetProfile(val profile: Profile) : QuickEditAction
    data class SetGear(val gear: Gear) : QuickEditAction
    data class SetDescription(val description: String?) : QuickEditAction
    data class SetWater(val water: Int?) : QuickEditAction
    data class SetEffort(val effort: Float?) : QuickEditAction
    data class SetFeel(val feel: Float?) : QuickEditAction
    data object Save : QuickEditAction
}

