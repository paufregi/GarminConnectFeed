package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import javax.inject.Inject

class UpdateActivity @Inject constructor(
    private val garminRepo: GarminRepository,
    private val stravaRepo: StravaRepository,
) {
    suspend operator fun invoke(
        activity: Activity,
        name: String?,
        description: String?,
        eventType: EventType?,
        course: Course?,
        water: Int?,
        feel: Float?,
        effort: Float?,
        workout: Workout?,
        gear: Gear?,
        trainingEffect: String?,
        trainingEffectFlag: Boolean,
    ): Result<Unit> = coroutineScope {
        if (name == null || (course != null && !activity.type.allowCourse))
            return@coroutineScope Result.failure("Validation error")

        val garminDeferred = async {
            garminRepo.updateActivity(
                activity = activity,
                name = name,
                description = Formatter.garminDescription(workout?.name),
                eventType = eventType,
                course = course,
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
                    name = name,
                    description = Formatter.stravaDescription(
                        description = description,
                        trainingEffect = trainingEffect.takeIf { trainingEffectFlag },
                        workout = workout?.name,
                    ),
                    commute = eventType?.commute,
                    gear = gear
                )
            } ?: Result.success(Unit)
        }

        val garminResult = runCatchingResult { garminDeferred.await() }
        val stravaResult = runCatchingResult { stravaDeferred.await() }

        if(garminResult.isFailure && stravaResult.isSuccess) return@coroutineScope Result.failure("Couldn't update Garmin activity")
        if(garminResult.isSuccess && stravaResult.isFailure) return@coroutineScope Result.failure("Couldn't update Strava activity")
        if(garminResult.isFailure && stravaResult.isFailure) return@coroutineScope Result.failure("Couldn't update Garmin & Strava activities")

        Result.success(Unit)
    }
}