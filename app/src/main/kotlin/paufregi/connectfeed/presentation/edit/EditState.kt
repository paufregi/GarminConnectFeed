package paufregi.connectfeed.presentation.edit

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.presentation.ui.models.ProcessState

data class EditState(
    val process: ProcessState = ProcessState.Processing,
    val activities: List<Activity> = emptyList(),
    val stravaActivities: List<Activity> = emptyList(),
    val eventTypes: List<EventType> = emptyList(),
    val courses: List<Course> = emptyList(),
    val activity: Activity? = null,
    val stravaActivity: Activity? = null,
    val name: String? = null,
    val eventType: EventType? = null,
    val course: Course? = null,
    val description: String? = null,
    val water: Int? = null,
    val effort: Float? = null,
    val feel: Float? = null,
    val trainingEffect: Boolean = false,
) {
    val hasStrava: Boolean
        get() = stravaActivities.isNotEmpty()
}

