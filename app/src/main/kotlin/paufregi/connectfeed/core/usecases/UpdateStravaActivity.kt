package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class UpdateStravaActivity @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        stravaActivity: Activity?,
        profile: Profile?,
        description: String?,
    ): Result<Unit> {
        if (stravaActivity == null || profile == null) return Result.Failure("Validation error")
        val name = if (profile.rename) profile.name else activity?.name
        val completeDescription =
            if (profile.trainingEffect == true && activity?.trainingEffect != null)
                "$description\n\nTraining: ${activity.trainingEffect}"
            else
                description
        val commute = profile.eventType?.let { it == EventType.Transportation }

        return garminRepository.updateStravaActivity(
            activity = stravaActivity,
            name = name,
            description = completeDescription,
            commute = commute
        )
    }
}