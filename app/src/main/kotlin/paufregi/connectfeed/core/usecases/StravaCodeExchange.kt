package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Named

class StravaCodeExchange @Inject constructor(
    private val authRepo: AuthRepository,
    @param:Named("StravaClientId") val clientId: String,
    @param:Named("StravaClientSecret") val clientSecret: String,
) {
    suspend operator fun invoke(code: String): Result<Unit> =
        authRepo.exchangeStravaToken(clientId, clientSecret, code)
            .onSuccess { authRepo.saveStravaToken(it) }
            .map { }
}