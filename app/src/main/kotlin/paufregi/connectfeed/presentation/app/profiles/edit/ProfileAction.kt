package paufregi.connectfeed.presentation.app.profiles.edit

import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType

sealed interface ProfileAction {
    data class SetName(val value: String) : ProfileAction
    data class SetType(val value: ActivityType) : ProfileAction
    data class SetEventType(val value: EventType?) : ProfileAction
    data class SetCourse(val value: Course?) : ProfileAction
    data class SetWater(val value: String) : ProfileAction
    data class SetRename(val value: Boolean) : ProfileAction
    data class SetCustomWater(val value: Boolean) : ProfileAction
    data class SetGear(val value: Boolean) : ProfileAction
    data class SetFeelAndEffort(val value: Boolean) : ProfileAction
    data class SetTrainingEffect(val value: Boolean) : ProfileAction
    data object Cancel : ProfileAction
    data object Save : ProfileAction
}

