package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class IsStravaConnected @Inject constructor(
    private val repo: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> =
        repo.getStravaToken().map { it != null }
}