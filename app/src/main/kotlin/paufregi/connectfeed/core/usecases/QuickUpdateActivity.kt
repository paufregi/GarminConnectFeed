package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class QuickUpdateActivity @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        profile: Profile?,
        feel: Float?,
        effort: Float?
    ): Result<Unit> {
        if (activity == null || profile == null) return Result.failure("Validation error")

        return garminRepository.updateActivity(
            activity = activity,
            name = if (profile.rename) profile.name else null,
            eventType = profile.eventType,
            course = profile.course,
            water = profile.water,
            feel = feel,
            effort = effort
        )
    }
}