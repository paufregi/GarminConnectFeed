package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class IsLoggedIn @Inject constructor (
    private val garminRepository: GarminRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> =
        combine(authRepository.getOAuth1(), garminRepository.getUser()) { oauth1, user ->
            oauth1?.isValid() == true && user != null
        }

}