package paufregi.connectfeed.presentation.profile

import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.ProfileType


sealed interface ProfileAction {
    data class SetName(val name: String) : ProfileAction
    data class SetType(val type: ProfileType) : ProfileAction
    data class SetEventType(val eventType: EventType?) : ProfileAction
    data class SetCourse(val course: Course?) : ProfileAction
    data class SetWater(val water: Int?) : ProfileAction
    data class SetRename(val rename: Boolean) : ProfileAction
    data class SetCustomWater(val customWater: Boolean) : ProfileAction
    data class SetFeelAndEffort(val feelAndEffort: Boolean) : ProfileAction
    data class SetTrainingEffect(val trainingEffect: Boolean) : ProfileAction
    data object Save : ProfileAction
}
