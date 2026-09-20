package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import java.time.Duration
import javax.inject.Inject

class GetActivities @Inject constructor(
    private val isStravaConnected: IsStravaConnected,
    private val garminRepo: GarminRepository,
    private val stravaRepo: StravaRepository,
) {
    suspend operator fun invoke(): Result<List<Activity>> = coroutineScope {
        val garminDeferred = async { garminRepo.getActivities(10) }
        val stravaDeferred = async {
            if (isStravaConnected().firstOrNull() == true) {
                stravaRepo.getActivities(15)
            } else {
                Result.success(emptyList())
            }
        }

        val garminResult = runCatchingResult { garminDeferred.await() }
        val stravaActivities = runCatchingResult { stravaDeferred.await() }.getOrDefault(emptyList())

        garminResult.map { garminActivities ->
            garminActivities.map { garminActivity ->
                val matchedStravaId = stravaActivities.find {
                    Duration.between(it.startDateTime, garminActivity.startDateTime).abs() <= Duration.ofMinutes(1) &&
                            it.sportType.type.compatible(garminActivity.type.type)
                }?.id

                Activity(
                    id = garminActivity.id,
                    name = garminActivity.name,
                    type = garminActivity.type.type,
                    eventType = garminActivity.eventType,
                    distance = garminActivity.distance,
                    trainingEffect = garminActivity.trainingEffectLabel,
                    date = garminActivity.startDateTime,
                    workoutId = garminActivity.workoutId,
                    stravaId = matchedStravaId,
                )
            }
        }
    }
}