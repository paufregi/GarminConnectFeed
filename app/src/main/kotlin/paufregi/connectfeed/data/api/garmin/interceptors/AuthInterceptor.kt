package paufregi.connectfeed.data.api.garmin.interceptors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.garmin.models.OAuth2
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return runBlocking(Dispatchers.IO) { getOrFetchOAuth2() }
            .fold(
                onSuccess = { chain.proceed(authRequest(request, it.accessToken)) },
                onFailure = { failedResponse(request, it.message ?: "Unknown error") }
            )
    }

    private suspend fun getOrFetchOAuth2(): Result<OAuth2> {
        val oAuth2 = authRepository.getOAuth2().firstOrNull()
        if (oAuth2 != null && !oAuth2.isExpired()) return Result.success(oAuth2)

        val consumer = authRepository.getOrFetchConsumer()
            ?: return Result.failure("Could not get OAuth Consumer")
        val oAuth1 = authRepository.getOAuth1().firstOrNull()
            ?: return Result.failure("No OAuth1 token found")

        val resOAuth2 = authRepository.exchange(consumer, oAuth1)
            .onSuccess { authRepository.saveOAuth2(it) }

        return resOAuth2
    }

    private fun authRequest(request: Request, accessToken: String?): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

    private fun failedResponse(request: Request, reason: String): Response =
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Auth failed")
            .body(reason.toResponseBody())
            .build()
}