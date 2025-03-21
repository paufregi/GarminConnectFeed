package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SyncStravaActivity @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        stravaActivity: Activity?,
        description: String?,
        trainingEffect: Boolean
    ): Result<Unit> {
        if (activity == null || stravaActivity == null) return Result.Failure("Validation error")
        val name = activity.name
        val completeDescription =
            if (trainingEffect == true && activity.trainingEffect != null)
                "$description\n\nTraining: ${activity.trainingEffect}"
            else
                description
        val commute = activity.eventType?.let { it == EventType.Transportation }

        return garminRepository.updateStravaActivity(
            activity = stravaActivity,
            name = name,
            description = completeDescription,
            commute = commute
        )
    }
}