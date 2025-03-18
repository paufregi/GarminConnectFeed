package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class RefreshUser @Inject constructor(
    private val garminRepository: GarminRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        garminRepository.fetchUser()
            .onSuccess { authRepository.saveUser(it) }
            .map { }
}