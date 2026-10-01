package paufregi.connectfeed.presentation.app.activities.edit

import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear

sealed interface ActivityAction {
    data class SetName(val name: String?) : ActivityAction
    data class SetEventType(val eventType: EventType) : ActivityAction
    data class SetCourse(val course: Course) : ActivityAction
    data class SetGear(val gear: Gear) : ActivityAction
    data class SetDescription(val description: String?) : ActivityAction
    data class SetWater(val water: Int?) : ActivityAction
    data class SetEffort(val effort: Float?) : ActivityAction
    data class SetFeel(val feel: Float?) : ActivityAction
    data class SetTrainingEffect(val trainingEffect: Boolean) : ActivityAction
    data object Save : ActivityAction
    data object Cancel : ActivityAction
}

