package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.StravaAuthRepository
import javax.inject.Inject
import javax.inject.Named

class StravaCodeExchange @Inject constructor (
    private val stravaAuthRepository: StravaAuthRepository,
    @Named("StravaClientId") val clientId: String,
    @Named("StravaClientSecret") val clientSecret: String,
) {
    suspend operator fun invoke(code: String): Result<Unit> =
        stravaAuthRepository.exchange(clientId, clientSecret, code)
            .onSuccess { stravaAuthRepository.saveToken(it) }
            .map { }
}