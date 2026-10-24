package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class SaveUser @Inject constructor(
    private val authRepo: AuthRepository,
) {
    suspend operator fun invoke(user: User): Unit = authRepo.saveUser(user)
}

