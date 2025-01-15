package paufregi.connectfeed.data.api.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.api.models.OAuth2
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val resOAuth2 = runBlocking(Dispatchers.IO) { getOrFetchOAuth2() }
        return when (resOAuth2) {
            is Result.Failure -> failedResponse(request, resOAuth2.reason)
            is Result.Success -> chain.proceed(authRequest(request, resOAuth2.data.accessToken))
        }
    }

    private suspend fun getOrFetchOAuth2(): Result<OAuth2> {
        val oAuth2 = authRepository.getOAuth2().firstOrNull()
        if (oAuth2 != null && !oAuth2.isExpired()) return Result.Success(oAuth2)

        val consumer = authRepository.getOrFetchConsumer() ?: return Result.Failure("Could not get OAuth Consumer")
        val oAuth1 = authRepository.getOAuth1().firstOrNull() ?: return Result.Failure("No OAuth1 token found")

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