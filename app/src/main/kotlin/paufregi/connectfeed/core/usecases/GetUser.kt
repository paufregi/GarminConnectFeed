package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class GetUser @Inject constructor (private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<User?> = authRepository.getUser()
}