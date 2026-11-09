package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class RefreshUser @Inject constructor(
    private val authRepo: AuthRepository,
    private val garminRepo: GarminRepository,
) {
    suspend operator fun invoke(): Result<Unit> =
        garminRepo.getUser()
            .onSuccess { authRepo.saveUser(it) }
            .map { }
}