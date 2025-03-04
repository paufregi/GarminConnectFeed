package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.data.repository.StravaAuthRepository
import javax.inject.Inject

class IsStravaLoggedIn @Inject constructor (
    private val authRepository: StravaAuthRepository
) {
    operator fun invoke(): Flow<Boolean> =
        authRepository.getToken().map { it != null }
}