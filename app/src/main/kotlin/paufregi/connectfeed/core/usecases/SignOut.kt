package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class SignOut @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() = authRepository.clear()
}