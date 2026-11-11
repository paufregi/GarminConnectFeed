package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class SignOut @Inject constructor(
    private val authRepo: AuthRepository,
) {
    suspend operator fun invoke() = authRepo.clear()
}