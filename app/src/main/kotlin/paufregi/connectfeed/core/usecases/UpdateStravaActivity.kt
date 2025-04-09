package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class UpdateStravaActivity @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        stravaActivity: Activity?,
        name: String?,
        description: String?,
        eventType: EventType?,
        trainingEffect: String?,
        trainingEffectFlag: Boolean,
    ): Result<Unit> {
        if (stravaActivity == null || name == null)
            return Result.Failure("Validation error")

        return garminRepository.updateStravaActivity(
            activity = stravaActivity,
            name = name,
            description = Formatter.description(description, trainingEffect, trainingEffectFlag),
            commute = eventType?.commute
        )
    }
}