package paufregi.connectfeed.presentation.app.activities.edit

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear

data class ActivityState(
    val loading: Boolean = false,

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
)

