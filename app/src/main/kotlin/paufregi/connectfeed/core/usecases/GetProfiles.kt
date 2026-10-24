package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class GetProfiles @Inject constructor(
    private val authRepo: AuthRepository,
    private val repo: AppRepository
) {
    // Switching user must drop the previous user's stream, not merge both.
    @ExperimentalCoroutinesApi
    operator fun invoke(): Flow<List<Profile>> =
        authRepo.getUser().flatMapLatest { user ->
            user?.let { repo.getAllProfiles(it) } ?: flowOf(emptyList())
        }
}