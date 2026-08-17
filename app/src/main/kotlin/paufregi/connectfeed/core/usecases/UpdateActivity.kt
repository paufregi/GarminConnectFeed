package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.core.utils.vo2max
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class UpdateActivity @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        name: String?,
        eventType: EventType?,
        course: Course?,
        water: Int?,
        feel: Float?,
        effort: Float?,
        workout: Workout?
    ): Result<Unit> {
        if (activity == null || name == null || (course != null && !activity.type.allowCourse))
            return Result.failure("Validation error")

        return garminRepository.updateActivity(
            activity = activity,
            name = name,
            description = workout?.name?.lowercase()?.vo2max(),
            eventType = eventType,
            course = course,
            water = water,
            feel = feel,
            effort = effort
        )
    }
}