package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject
import javax.inject.Named

class SignIn @Inject constructor(
    private val authRepo: AuthRepository,
    private val repo: GarminRepository,
    @param:Named("garminClientId") val garminClientId: String
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank() || password.isBlank()) return Result.failure("Validation error")

        return authRepo.garminLogin(username, password)
            .andThen {
                authRepo.exchangeGarminToken(it, garminClientId)
                    .onSuccess { authRepo.saveGarminToken(it) }
            }
            .andThen {
                repo.getUserProfile()
                    .onSuccess { authRepo.saveUser(it) }
            }
            .onFailure { authRepo.clear() }
    }
}

