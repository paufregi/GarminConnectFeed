package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AppRepository
import javax.inject.Inject

class GetProfile @Inject constructor(private val repo: AppRepository) {
    operator fun invoke(id: Long?): Flow<Profile?> =
        id?.let { repo.getProfile(id) } ?: flowOf(Profile())
}