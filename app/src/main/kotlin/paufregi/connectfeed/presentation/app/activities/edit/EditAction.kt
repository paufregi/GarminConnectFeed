package paufregi.connectfeed.presentation.app.activities.edit

import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear

sealed interface EditAction {
    data class SetName(val name: String?): EditAction
    data class SetEventType(val eventType: EventType): EditAction
    data class SetCourse(val course: Course): EditAction
    data class SetGear(val gear: Gear): EditAction
    data class SetDescription(val description: String?): EditAction
    data class SetWater(val water: Int?): EditAction
    data class SetEffort(val effort: Float?): EditAction
    data class SetFeel(val feel: Float?): EditAction
    data class SetTrainingEffect(val trainingEffect: Boolean): EditAction
    data object ResetStatus: EditAction
    data object Save : EditAction
}

