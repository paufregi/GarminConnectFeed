package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.StravaAuthRepository
import javax.inject.Inject

class SaveStravaCode @Inject constructor (private val stravaAuthRepository: StravaAuthRepository) {
    suspend operator fun invoke(code: String) = stravaAuthRepository.saveCode(code)
}