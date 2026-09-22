package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import javax.inject.Inject
import paufregi.connectfeed.core.models.Gear as CoreGear

class SyncGear @Inject constructor(
    private val authRepo: AuthRepository,
    private val isStravaConnected: IsStravaConnected,
    private val garminRepo: GarminRepository,
    private val stravaRepo: StravaRepository,
    private val appRepo: AppRepository,
) {
    suspend operator fun invoke(): Result<Unit> = coroutineScope {
        val user = authRepo.getUser().firstOrNull()
            ?: return@coroutineScope Result.failure(Exception("User must be logged in"))

        val garminDeferred = async { garminRepo.getGears() }
        val stravaDeferred = async {
            isStravaConnected().firstOrNull()?.let {
                stravaRepo.getAthlete()
            } ?: Result.success(Athlete(0))
        }

        val garminResult = runCatchingResult { garminDeferred.await() }
        val stravaGears = runCatchingResult { stravaDeferred.await() }.getOrDefault(Athlete(0))

        garminResult.map { garminGears ->
            garminGears.map { garminGear ->
                val matchedStravaId = when (garminGear.type) {
                    GearType.Bike -> stravaGears.bikes.find { it.name == garminGear.name || it.name == "${garminGear.model} ${garminGear.model}" }?.id
                    GearType.Shoe -> stravaGears.shoes.find { it.name == garminGear.name || it.name == "${garminGear.model} ${garminGear.model}" }?.id
                    GearType.Unknown -> null
                }

                CoreGear(
                    id = garminGear.id,
                    name = garminGear.name ?: "${garminGear.model} ${garminGear.model}",
                    type = garminGear.type,
                    stravaId = matchedStravaId,
                )
            }
        }.andThen { gears ->
                val existingGears = appRepo.getAllGears(user).firstOrNull() ?: emptyList()
                val newGearIds = gears.map { it.id }.toSet()

            runCatching {
                existingGears
                    .filterNot { it.id in newGearIds }
                    .forEach { appRepo.deleteGear(user, it) }

                gears.forEach { appRepo.saveGear(user, it) }
            }
        }
    }
}
