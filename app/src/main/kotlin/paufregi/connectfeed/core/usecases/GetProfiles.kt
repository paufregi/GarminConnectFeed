package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetProfiles @Inject constructor(
    private val authRepository: AuthRepository,
    private val garminRepository: GarminRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Profile>> =
        authRepository.getUser().flatMapMerge { user ->
            user?.let { garminRepository.getAllProfiles(it) } ?: flowOf(emptyList())
        }
}