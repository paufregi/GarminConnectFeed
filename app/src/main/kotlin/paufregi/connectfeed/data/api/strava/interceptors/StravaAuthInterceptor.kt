package paufregi.connectfeed.data.api.strava.interceptors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.strava.models.Token
import paufregi.connectfeed.data.api.utils.authRequest
import paufregi.connectfeed.data.api.utils.failedAuthResponse
import paufregi.connectfeed.data.repository.StravaAuthRepository
import javax.inject.Inject
import javax.inject.Named

class StravaAuthInterceptor @Inject constructor(
    private val stravaRepo: StravaAuthRepository,
    @Named("StravaClientId") val clientId: String,
    @Named("StravaClientSecret") val clientSecret: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        runBlocking(Dispatchers.IO) {
            getOrRefreshToken().fold(
                onSuccess = { chain.proceed(authRequest(chain.request(), it.accessToken)) },
                onFailure = { failedAuthResponse(chain.request(), it.message ?: "Unknown error") }
            )
        }

    private suspend fun getOrRefreshToken(): Result<Token> {
        val token = stravaRepo.getToken().firstOrNull()

        if (token == null) return Result.failure("No token found")
        if (!token.isExpired()) return Result.success(token)

        return stravaRepo.refresh(clientId, clientSecret, token.refreshToken)
            .onSuccess { stravaRepo.saveToken(it) }
    }
}