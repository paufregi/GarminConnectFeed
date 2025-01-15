package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class IsLoggedIn @Inject constructor (
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> =
        authRepository.getUser().map { it != null }
}