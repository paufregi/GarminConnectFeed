package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class GetProfile @Inject constructor(
    private val authRepo: AuthRepository,
    private val repo: AppRepository,
) {
    suspend operator fun invoke(id: Long?): Profile? {
        if (id == null) return null
        val user = authRepo.getUser().firstOrNull() ?: return null
        return repo.getProfile(user, id)
    }
}

