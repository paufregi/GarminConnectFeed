package paufregi.connectfeed.data.api.garmin.interceptors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.garmin.models.OAuth2
import paufregi.connectfeed.data.api.utils.authRequest
import paufregi.connectfeed.data.api.utils.failedAuthResponse
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response  =
        runBlocking(Dispatchers.IO) {
            getOrFetchOAuth2().fold(
                onSuccess = { chain.proceed(authRequest(chain.request(), it.accessToken)) },
                onFailure = { failedAuthResponse(chain.request(), it.message ?: "Unknown error") }
            )
        }

    private suspend fun getOrFetchOAuth2(): Result<OAuth2> {
        val oAuth2 = authRepository.getOAuth2().firstOrNull()
        if (oAuth2 != null && !oAuth2.isExpired()) return Result.success(oAuth2)

        val consumer = authRepository.getOrFetchConsumer()
            ?: return Result.failure("Could not get OAuth Consumer")
        val oAuth1 = authRepository.getOAuth1().firstOrNull()
            ?: return Result.failure("No OAuth1 token found")

        return authRepository.exchange(consumer, oAuth1)
            .onSuccess { authRepository.saveOAuth2(it) }
    }
}