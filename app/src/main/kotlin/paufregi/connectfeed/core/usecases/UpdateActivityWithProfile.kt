package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import javax.inject.Inject

class UpdateActivityWithProfile @Inject constructor(
    private val garminRepo: GarminRepository,
    private val stravaRepo: StravaRepository,
) {
    suspend operator fun invoke(
        activity: Activity,
        profile: Profile?,
        description: String?,
        water: Int?,
        feel: Float?,
        effort: Float?,
        workout: Workout?,
        gear: Gear?,
        trainingEffect: String?,
    ): Result<Unit> = coroutineScope {
        if (profile == null)
            return@coroutineScope Result.failure("Validation error")

        val garminDeferred = async {
            garminRepo.updateActivity(
                activity = activity,
                name = profile.name.takeIf { profile.rename },
                description = Formatter.garminDescription(workout?.name),
                eventType = profile.eventType,
                course = profile.course,
                water = water,
                feel = feel,
                effort = effort,
                gear = gear,
            )
        }

        val stravaDeferred = async {
            activity.stravaId?.let {
                stravaRepo.updateActivity(
                    activity = activity,
                    name = profile.name.takeIf { profile.rename },
                    description = Formatter.stravaDescription(
                        description = description,
                        trainingEffect = trainingEffect.takeIf { profile.trainingEffect },
                        workout = workout?.name,
                    ),
                    commute = profile.eventType?.commute,
                    gear = gear,
                )
            } ?: Result.success(Unit)
        }

        val garminResult = runCatchingResult { garminDeferred.await() }
        val stravaResult = runCatchingResult { stravaDeferred.await() }

        if(garminResult.isFailure && stravaResult.isSuccess) return@coroutineScope Result.failure("Couldn't update Garmin activity")
        if(garminResult.isSuccess && stravaResult.isFailure) return@coroutineScope Result.failure("Couldn't update Strava activity")
        if(garminResult.isFailure && stravaResult.isFailure) return@coroutineScope Result.failure("Couldn't update Garmin and Strava activities")

        Result.success(Unit)
    }
}