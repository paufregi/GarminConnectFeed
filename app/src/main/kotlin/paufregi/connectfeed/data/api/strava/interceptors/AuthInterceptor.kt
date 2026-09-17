package paufregi.connectfeed.data.api.strava.interceptors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.strava.models.AuthToken
import paufregi.connectfeed.data.api.utils.authRequest
import paufregi.connectfeed.data.api.utils.failedAuthResponse
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(
    private val repo: AuthRepository,
    @param:Named("StravaClientId") val clientId: String,
    @param:Named("StravaClientSecret") val clientSecret: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        runBlocking(Dispatchers.IO) {
            getOrRefreshToken().fold(
                onSuccess = { chain.proceed(authRequest(chain.request(), it.accessToken)) },
                onFailure = { failedAuthResponse(chain.request(), it.message ?: "Unknown error") }
            )
        }

    private suspend fun getOrRefreshToken(): Result<AuthToken> =
        repo.getStravaToken().firstOrNull()?.let { token ->
            if (!token.isExpired()) Result.success(token)
            else repo.stravaRefreshToken(clientId, clientSecret, token.refreshToken)
                .onSuccess { repo.saveStravaToken(it) }
        } ?: Result.failure("No token found")
}