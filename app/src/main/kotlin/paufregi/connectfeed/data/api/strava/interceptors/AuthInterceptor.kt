package paufregi.connectfeed.data.api.strava.interceptors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.strava.models.AuthTokenDto
import paufregi.connectfeed.data.api.utils.authRequest
import paufregi.connectfeed.data.api.utils.failedAuthResponse
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val repo: AuthRepository,
    @param:Named("StravaClientId") val clientId: String,
    @param:Named("StravaClientSecret") val clientSecret: String,
) : Interceptor {

    // Parallel requests must not each refresh an expired token; the first one wins and
    // the rest re-read the stored token once the lock is released.
    private val refreshMutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response =
        runBlocking(Dispatchers.IO) {
            getOrRefreshToken().fold(
                onSuccess = { chain.proceed(authRequest(chain.request(), it.accessToken)) },
                onFailure = { failedAuthResponse(chain.request(), it.message ?: "Unknown error") }
            )
        }

    private suspend fun getOrRefreshToken(): Result<AuthTokenDto> {
        val token = repo.getStravaToken().firstOrNull() ?: return Result.failure("No token found")
        if (!token.isExpired()) return Result.success(token)

        return refreshMutex.withLock {
            val current = repo.getStravaToken().firstOrNull() ?: return Result.failure("No token found")
            if (!current.isExpired()) return Result.success(current)

            repo.refreshStravaToken(clientId, clientSecret, current.refreshToken)
                .onSuccess { repo.saveStravaToken(it) }
        }
    }
}