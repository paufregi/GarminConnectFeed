package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.StravaRepository
import javax.inject.Inject

class UpdateStravaActivity @Inject constructor(private val stravaRepository: StravaRepository) {
    suspend operator fun invoke(
        stravaActivity: Activity?,
        name: String?,
        description: String?,
        eventType: EventType?,
        trainingEffect: String?,
        trainingEffectFlag: Boolean,
        workout: Workout?,
        gear: Gear?,
    ): Result<Unit> {
        if (stravaActivity == null || name == null)
            return Result.failure("Validation error")

        return stravaRepository.updateActivity(
            activity = stravaActivity,
            name = name,
            description = Formatter.description(
                description = description,
                trainingEffect = trainingEffect,
                trainingEffectFlag = trainingEffectFlag,
                workout = workout?.name,
            ),
            commute = eventType?.commute,
            gear = gear
        )
    }
}