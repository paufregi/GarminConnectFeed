package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject
import javax.inject.Named

class SignIn @Inject constructor(
    private val garminRepository: GarminRepository,
    private val authRepository: AuthRepository,
    @param:Named("GarminClientId") val garminClientId: String
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank() || password.isBlank()) return Result.failure("Validation error")

        return authRepository.garminLogin(username, password)
            .andThen { authRepository.exchangeGarminToken(it, garminClientId) }
            .onSuccess { authRepository.saveGarminToken(it) }
            .andThen { garminRepository.fetchUser() }
            .onFailure { authRepository.clear() }
            .onSuccess { authRepository.saveUser(it) }
    }
}

