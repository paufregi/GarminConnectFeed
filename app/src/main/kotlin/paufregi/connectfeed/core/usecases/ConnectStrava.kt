package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Named

class ConnectStrava @Inject constructor(
    private val repo: AuthRepository,
    @param:Named("StravaClientId") val clientId: String,
    @param:Named("StravaClientSecret") val clientSecret: String,
) {
    suspend operator fun invoke(code: String): Result<Unit> =
        repo.exchangeStravaToken(clientId, clientSecret, code)
            .onSuccess { repo.saveStravaToken(it) }
            .map { }
}