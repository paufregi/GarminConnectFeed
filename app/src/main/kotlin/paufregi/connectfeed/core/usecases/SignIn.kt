package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SignIn @Inject constructor (
    private val authRepository: AuthRepository,
    private val garminRepository: GarminRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank() && password.isBlank()) return Result.Failure("Validation error")

        val consumer = authRepository.getOrFetchConsumer()
            ?: return Result.Failure("Couldn't get OAuth Consumer")

        val resOAuth1 = authRepository.authorize(username, password, consumer)
        when (resOAuth1) {
            is Result.Success -> authRepository.saveOAuth1(resOAuth1.data)
            is Result.Failure -> return Result.Failure(resOAuth1.reason)
        }

        val resUser = garminRepository.fetchUser()
        when (resUser) {
            is Result.Success -> garminRepository.saveUser(resUser.data)
            is Result.Failure -> authRepository.deleteOAuth1()
        }

        return resUser
    }
}

