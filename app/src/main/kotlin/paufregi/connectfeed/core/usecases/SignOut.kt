package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SignOut @Inject constructor (
    private val authRepository: AuthRepository,
    private val garminRepository: GarminRepository
) {
    suspend operator fun invoke() {
        authRepository.clear()
        garminRepository.deleteUser()
    }
}