package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.StravaAuthRepository
import javax.inject.Inject

class DisconnectStrava @Inject constructor(
    private val authRepository: StravaAuthRepository
) {
    suspend operator fun invoke() = authRepository.clear()
}