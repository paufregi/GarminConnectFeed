package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SyncGear @Inject constructor(
    private val authRepository: AuthRepository,
    private val garminRepository: GarminRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val user = authRepository.getUser().firstOrNull()
            ?: return Result.failure(Exception("User must be logged in"))

        val gears = garminRepository.getGears().getOrElse {
            return Result.failure(it)
        }

        val currentGears = garminRepository.getAllGears(user).first()
        val apiGearIds = gears.map { it.id }.toSet()

        currentGears
            .filterNot { it.id in apiGearIds }
            .forEach { garminRepository.deleteGear(user, it) }

        gears.forEach { garminRepository.saveGear(user, it) }

        return Result.success(Unit)
    }
}
