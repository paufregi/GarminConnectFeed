package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetGears @Inject constructor(
    private val authRepo: AuthRepository,
    private val appRepo: AppRepository
) {
    operator fun invoke(): Flow<List<Gear>> =
        authRepo.getUser().flatMapMerge { user ->
            user?.let { appRepo.getAllGears(it) } ?: flowOf(emptyList())
        }
}
