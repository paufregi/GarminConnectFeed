package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SignOut @Inject constructor (private val garminRepository: GarminRepository) {
    suspend operator fun invoke() {
        garminRepository.deleteTokens()
        garminRepository.deleteCredential()
        garminRepository.deleteUser()
    }
}