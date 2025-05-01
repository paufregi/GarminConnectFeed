package paufregi.connectfeed.data.api.garmin.interceptors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import paufregi.connectfeed.data.api.utils.authRequest
import paufregi.connectfeed.data.api.utils.failedAuthResponse
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response  =
        runBlocking(Dispatchers.IO) {
            getOrFetchAuthToken().fold(
                onSuccess = { chain.proceed(authRequest(chain.request(), it.accessToken)) },
                onFailure = { failedAuthResponse(chain.request(), it.message ?: "Unknown error") }
            )
        }

    private suspend fun getOrFetchAuthToken(): Result<AuthToken> {
        val token = authRepository.getAuthToken().firstOrNull()
        if (token != null && !token.isExpired()) return Result.success(token)

        val consumer = authRepository.getOrFetchConsumer()
            ?: return Result.failure("Could not get Consumer")
        val preAuth = authRepository.getPreAuth().firstOrNull()
            ?: return Result.failure("No PreAuth token found")

        return authRepository.exchange(consumer, preAuth)
            .onSuccess { authRepository.saveAuthToken(it) }
    }
}