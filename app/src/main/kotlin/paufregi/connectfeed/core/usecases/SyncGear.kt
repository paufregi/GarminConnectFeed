package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import javax.inject.Inject

class SyncGear @Inject constructor(
    private val isStravaConnected: IsStravaConnected,
    private val garminRepo: GarminRepository,
    private val stravaRepo: StravaRepository,
) {
    suspend operator fun invoke(): Result<Unit> = coroutineScope {
        val garminDeferred = async { garminRepo.getGears() }
        val stravaDeferred = async {
            isStravaConnected().firstOrNull()?.let {
                stravaRepo.getAthlete()
            } ?: Result.success(Athlete(0))
        }

        val garminResult = runCatchingResult { garminDeferred.await() }
        val stravaActivities = runCatchingResult { stravaDeferred.await() }.getOrDefault(Athlete(0))

        garminResult.map { garminGears ->
            garminGears.map { garminGear ->
                val matchedStravaId = when(garminGear.type) {
                    GearType.Bike -> stravaActivities.bikes.find { it.name == garminGear.name || it.name == "${garminGear.model} ${garminGear.model}" }?.id
                    GearType.Shoe -> stravaActivities.shoes.find { it.name == garminGear.name || it.name == "${garminGear.model} ${garminGear.model}" }?.id
                    GearType.Unknown -> null
                }

                Gear(
                    id = garminGear.id,
                    name = garminGear.name ?: "${garminGear.model} ${garminGear.model}" ,
                    type = garminGear.type,
                    stravaId = matchedStravaId,
                )
            }
        }

        Result.success(Unit)
    }
}
