package paufregi.connectfeed.presentation.modify

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Profile

sealed interface ModifyAction {
    data class SetActivity(val activity: Activity) : ModifyAction
    data object ClearActivity : ModifyAction
    data class SetMode(val mode: ModifyMode) : ModifyAction
    data class SetName(val name: String?) : ModifyAction
    data class SetEventType(val eventType: EventType) : ModifyAction
    data class SetCourse(val course: Course?) : ModifyAction
    data class SetProfile(val profile: Profile) : ModifyAction
    data class SetGear(val gear: Gear?) : ModifyAction
    data class SetDescription(val description: String?) : ModifyAction
    data class SetWater(val water: Int?) : ModifyAction
    data class SetEffort(val effort: Float?) : ModifyAction
    data class SetFeel(val feel: Float?) : ModifyAction
    data class SetTrainingEffect(val trainingEffect: Boolean) : ModifyAction
    data object Save : ModifyAction
    data object Restart : ModifyAction
}
