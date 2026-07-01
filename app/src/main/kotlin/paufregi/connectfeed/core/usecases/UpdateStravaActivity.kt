package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
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
        workoutId: Long?,
    ): Result<Unit> {
        if (stravaActivity == null || name == null)
            return Result.failure("Validation error")

        val workout = workoutId?.let { id ->
            garminRepository.getWorkout(id).getOrNull()
        }

        return garminRepository.updateStravaActivity(
            activity = stravaActivity,
            name = name,
            description = Formatter.description(
                description = description,
                trainingEffect = trainingEffect,
                trainingEffectFlag = trainingEffectFlag,
                workout = workout?.name,
            ),
            commute = eventType?.commute
        )
    }
}