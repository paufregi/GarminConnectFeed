package paufregi.connectfeed.presentation.modify

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.ProcessState

enum class ModifyMode {
    Manual,
    Profile,
}

data class ModifyState(
    val process: ProcessState = ProcessState.Processing,
    val activities: List<Activity> = emptyList(),
    val stravaActivities: List<Activity> = emptyList(),
    val profiles: List<Profile> = emptyList(),
    val eventTypes: List<EventType> = emptyList(),
    val courses: List<Course> = emptyList(),
    val gears: List<Gear> = emptyList(),
    val activity: Activity? = null,
    val stravaActivity: Activity? = null,
    val mode: ModifyMode = ModifyMode.Manual,
    val name: String? = null,
    val eventType: EventType? = null,
    val course: Course? = null,
    val profile: Profile? = null,
    val gear: Gear? = null,
    val description: String? = null,
    val water: Int? = null,
    val effort: Float? = null,
    val feel: Float? = null,
    val trainingEffect: Boolean = false,
) {
    val hasStrava: Boolean
        get() = stravaActivities.isNotEmpty()

    val canSave: Boolean
        get() = when (mode) {
            ModifyMode.Manual -> activity != null
            ModifyMode.Profile -> activity != null && profile != null
        }
}
