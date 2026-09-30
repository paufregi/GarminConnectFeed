package paufregi.connectfeed.presentation.app.profiles.edit

import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile

data class ProfileState(
    val loading: Boolean = true,
    val profile: Profile = Profile(),
    val activityTypes: List<ActivityType> = emptyList(),
    val eventTypes: List<EventType> = emptyList(),
    val courses: List<Course> = emptyList(),
) {
    val availableCourses: List<Course>
        get() = courses.filter { it.type.compatible(profile.type) }

    val canSave: Boolean
        get() = profile.name.isNotBlank() &&
                (profile.type == ActivityType.Any || profile.eventType != null)
}
