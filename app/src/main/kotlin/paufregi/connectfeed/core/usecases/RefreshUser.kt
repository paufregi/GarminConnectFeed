package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class RefreshUser @Inject constructor(
    private val authRepo: AuthRepository,
    private val repo: GarminRepository,
) {
    suspend operator fun invoke(): Result<Unit> =
        repo.getUserProfile()
            .onSuccess { authRepo.saveUser(it) }
            .map { }
}