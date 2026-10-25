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
        trainingEffect: Boolean,
    ): Result<Unit> = coroutineScope {
        if (name.isNullOrBlank() || (course != null && !(activity.type.allowCourse && course.compatible(activity))))
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
                        trainingEffect = activity.trainingEffect.takeIf { trainingEffect },
                        workout = workout?.name,
                    ),
                    commute = eventType?.commute,
                    gear = gear
                )
            } ?: Result.success(Unit)
        }

        val garminResult = garminDeferred.await()
        val stravaResult = stravaDeferred.await()

        when {
            garminResult.isFailure && stravaResult.isFailure ->
                Result.failure("Couldn't update Garmin & Strava activities", garminResult.exceptionOrNull(), stravaResult.exceptionOrNull())
            garminResult.isFailure -> Result.failure("Couldn't update Garmin activity", garminResult.exceptionOrNull())
            stravaResult.isFailure -> Result.failure("Couldn't update Strava activity", stravaResult.exceptionOrNull())
            else -> Result.success(Unit)
        }
    }
}