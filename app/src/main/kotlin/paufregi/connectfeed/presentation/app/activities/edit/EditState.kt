package paufregi.connectfeed.presentation.app.activities.edit

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear

sealed interface Status {
    data object Loading : Status
    data object Success: Status
    data class Failure(val reason: String) : Status
}

data class EditState(
    val status: Status? = Status.Loading,

    val activity: Activity,
    val eventTypes: List<EventType> = emptyList(),
    val courses: List<Course> = emptyList(),
    val gears: List<Gear> = emptyList(),

    val name: String? = null,
    val eventType: EventType? = null,
    val course: Course? = null,
    val gear: Gear? = null,
    val description: String? = null,
    val water: Int? = null,
    val effort: Float? = null,
    val feel: Float? = null,
    val trainingEffect: Boolean = false,
) {
    val availableCourses: List<Course>
        get() = courses.filter { it.type.compatible(activity.type) }

    val availableGears: List<Gear>
        get() = gears.filter { it.type.compatible(activity.type) }
}

