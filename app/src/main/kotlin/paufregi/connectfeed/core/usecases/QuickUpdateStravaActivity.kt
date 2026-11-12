package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.StravaRepository
import javax.inject.Inject

class QuickUpdateStravaActivity @Inject constructor(private val stravaRepo: StravaRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        stravaActivity: Activity?,
        profile: Profile?,
        description: String?,
        workout: Workout?,
        gear: Gear?,
    ): Result<Unit> {
        if (stravaActivity == null || profile == null) return Result.failure("Validation error")

        return stravaRepo.updateActivity(
            activity = stravaActivity,
            name = if (profile.rename) profile.name else activity?.name,
            description = Formatter.description(description, activity?.trainingEffect, profile.trainingEffect, workout?.name),
            commute = profile.eventType?.commute,
            gear = gear
        )
    }
}