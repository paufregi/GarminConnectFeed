package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class RefreshUser @Inject constructor (private val garminRepository: GarminRepository) {
    suspend operator fun invoke(): Result<Unit> =
        garminRepository.fetchUser()
            .onSuccess { garminRepository.saveUser(it) }
            .map {  }
}