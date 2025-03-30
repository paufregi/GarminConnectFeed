package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class QuickUpdateStravaActivity @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        stravaActivity: Activity?,
        profile: Profile?,
        description: String?,
    ): Result<Unit> {
        if (stravaActivity == null || profile == null) return Result.Failure("Validation error")

        return garminRepository.updateStravaActivity(
            activity = stravaActivity,
            name = if (profile.rename) profile.name else activity?.name,
            description = Formatter.description(description, activity?.trainingEffect, profile.trainingEffect),
            commute = profile.eventType?.commute
        )
    }
}