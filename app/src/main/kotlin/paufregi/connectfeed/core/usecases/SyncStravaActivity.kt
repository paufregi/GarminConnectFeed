package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SyncStravaActivity @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        stravaActivity: Activity?,
        description: String?,
        trainingEffect: Boolean
    ): Result<Unit> {
        if (activity == null || stravaActivity == null) return Result.failure("Validation error")

        return garminRepository.updateStravaActivity(
            activity = stravaActivity,
            name = activity.name,
            description = Formatter.description(description, activity.trainingEffect, trainingEffect),
            commute = activity.eventType?.commute
        )
    }
}