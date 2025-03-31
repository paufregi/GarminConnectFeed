package paufregi.connectfeed.presentation.edit

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType

sealed interface EditAction {
    data class SetActivity(val activity: Activity) : EditAction
    data class SetStravaActivity(val activity: Activity) : EditAction
    data class SetEventType(val eventType: EventType) : EditAction
    data class SetCourse(val course: Course?) : EditAction
    data class SetDescription(val description: String?) : EditAction
    data class SetWater(val water: Int?) : EditAction
    data class SetEffort(val effort: Float?) : EditAction
    data class SetFeel(val feel: Float?) : EditAction
    data class SetTrainingEffect(val trainingEffect: Boolean) : EditAction
    data object Save : EditAction
    data object Restart : EditAction
}
