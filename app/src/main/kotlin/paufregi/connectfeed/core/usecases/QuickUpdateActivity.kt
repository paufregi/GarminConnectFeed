package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class QuickUpdateActivity @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        profile: Profile?,
        water: Int?,
        feel: Float?,
        effort: Float?,
        workout: Workout?,
    ): Result<Unit> {
        if (activity == null || profile == null) return Result.failure("Validation error")

        return garminRepository.updateActivity(
            activity = activity,
            name = if (profile.rename) profile.name else null,
            description = Formatter.workout(workout?.name),
            eventType = profile.eventType,
            course = profile.course,
            water = water,
            feel = feel,
            effort = effort,
            gears = null,
        )
    }
}