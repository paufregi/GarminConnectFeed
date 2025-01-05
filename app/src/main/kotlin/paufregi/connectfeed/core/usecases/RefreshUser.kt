package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class RefreshUser @Inject constructor (private val garminRepository: GarminRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return when (val res = garminRepository.fetchUser()) {
            is Result.Success -> {
                garminRepository.saveUser(res.data!!)
                Result.Success(Unit)
            }
            is Result.Failure -> Result.Failure(res.reason)
        }
    }
}