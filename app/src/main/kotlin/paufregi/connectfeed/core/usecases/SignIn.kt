package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SignIn @Inject constructor(
    private val garminRepository: GarminRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank() || password.isBlank()) return Result.failure("Validation error")

        val consumer = authRepository.getOrFetchConsumer()
            ?: return Result.failure("Couldn't get OAuth Consumer")

        val resOAuth1 = authRepository.authorize(username, password, consumer)
            .onSuccess { authRepository.saveOAuth1(it) }
            .onFailure { authRepository.clear() }
            .exceptionOrNull()

        if (resOAuth1 != null) return Result.failure(resOAuth1)

        val resUser = garminRepository.fetchUser()
            .onSuccess { authRepository.saveUser(it) }
            .onFailure { authRepository.clear() }

        return resUser
    }
}

