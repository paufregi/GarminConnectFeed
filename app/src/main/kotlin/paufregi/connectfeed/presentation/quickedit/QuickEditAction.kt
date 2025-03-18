package paufregi.connectfeed.presentation.quickedit

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Profile

sealed interface QuickEditAction {
    data class SetActivity(val activity: Activity) : QuickEditAction
    data class SetStravaActivity(val activity: Activity) : QuickEditAction
    data class SetProfile(val profile: Profile) : QuickEditAction
    data class SetDescription(val description: String?) : QuickEditAction
    data class SetWater(val water: Int?) : QuickEditAction
    data class SetEffort(val effort: Float?) : QuickEditAction
    data class SetFeel(val feel: Float?) : QuickEditAction
    data object Save : QuickEditAction
    data object Restart : QuickEditAction
}
