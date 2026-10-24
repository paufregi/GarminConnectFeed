package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetActivity @Inject constructor(
    private val garminRepo: GarminRepository,
) {
    suspend operator fun invoke(garminId: Long, stravaId: Long?): Result<Activity> =
        garminRepo.getActivity(garminId).map { activity ->
            Activity(
                id = activity.id,
                name = activity.name,
                type = activity.type.type,
                eventType = activity.eventType,
                distance = activity.distance.takeIf { it > 0 },
                trainingEffect = activity.trainingEffectLabel,
                date = activity.startDateTime,
                workoutId = activity.workoutId,
                stravaId = stravaId,
            )
        }
}