package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class DeleteProfile @Inject constructor(
    private val authRepo: AuthRepository,
    private val repo: AppRepository
) {
    suspend operator fun invoke(profile: Profile) =
        authRepo.getUser().firstOrNull()?.let {
            repo.deleteProfile(it, profile)
        }
}