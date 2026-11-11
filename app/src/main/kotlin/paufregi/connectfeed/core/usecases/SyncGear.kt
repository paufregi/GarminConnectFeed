package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.equalsAny
import paufregi.connectfeed.core.utils.mapList
import paufregi.connectfeed.data.api.strava.models.AthleteDto
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import javax.inject.Inject

class SyncGear @Inject constructor(
    private val authRepo: AuthRepository,
    private val appRepo: AppRepository,
    private val garminRepo: GarminRepository,
    private val stravaRepo: StravaRepository,
    private val isStravaLoggedIn: IsStravaLoggedIn,
) {
    suspend operator fun invoke(): Result<Unit> = coroutineScope {
        val user = authRepo.getUser().firstOrNull()
            ?: return@coroutineScope Result.failure(Exception("User must be logged in"))

        val garminDeferred = async { garminRepo.getGears() }
        val stravaDeferred = async {
            when (isStravaLoggedIn().firstOrNull()) {
                true -> stravaRepo.getAthlete()
                else -> Result.success(AthleteDto(0))
            }
        }

        val gears = garminDeferred.await()
        val stravaGears = stravaDeferred.await().getOrDefault(AthleteDto(0))

        gears.mapList { gear ->
            val alias = "${gear.brand} ${gear.model}"
            val stravaId = when (gear.type) {
                GearType.Bike -> stravaGears.bikes.find {
                    it.name != null && it.name.equalsAny(gear.name, alias, ignoreCase = true)
                }?.id
                GearType.Shoe -> stravaGears.shoes.find {
                    it.name != null && it.name.equalsAny(gear.name, alias, ignoreCase = true)
                }?.id
                GearType.Unknown -> null
            }

            Gear(
                id = gear.id,
                name = gear.name ?: alias,
                type = gear.type,
                distance = gear.distance?.toInt(),
                stravaId = stravaId,
            )
        }.andThen { gears ->
            val dbGears = appRepo.getAllGears(user).first()
            val apiGearIds = gears.map { it.id }

            dbGears
                .filterNot { it.id in apiGearIds }
                .forEach { appRepo.deleteGear(user, it) }

            gears.forEach { appRepo.saveGear(user, it) }

            Result.success(Unit)
        }
    }
}
