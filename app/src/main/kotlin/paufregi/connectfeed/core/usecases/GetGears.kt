package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetGears @Inject constructor(
    private val authRepo: AuthRepository,
    private val repo: AppRepository
) {
    // Switching user must drop the previous user's stream, not merge both.
    operator fun invoke(): Flow<List<Gear>> =
        authRepo.getUser().flatMapLatest { user ->
            user?.let { repo.getAllGears(it) } ?: flowOf(emptyList())
        }
}
