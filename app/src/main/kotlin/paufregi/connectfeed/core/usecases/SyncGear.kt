package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SyncGear @Inject constructor(
    private val authRepo: AuthRepository,
    private val appRepo: AppRepository,
    private val garminRepo: GarminRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val user = authRepo.getUser().firstOrNull()
            ?: return Result.failure(Exception("User must be logged in"))

        val gears = garminRepo.getGears().getOrElse {
            return Result.failure(it)
        }

        val currentGears = appRepo.getAllGears(user).first()
        val apiGearIds = gears.map { it.id }.toSet()

        currentGears
            .filterNot { it.id in apiGearIds }
            .forEach { appRepo.deleteGear(user, it) }

        gears.forEach { appRepo.saveGear(user, it) }

        return Result.success(Unit)
    }
}
