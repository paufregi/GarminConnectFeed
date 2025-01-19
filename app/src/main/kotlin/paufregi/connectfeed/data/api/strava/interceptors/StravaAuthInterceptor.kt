package paufregi.connectfeed.data.api.strava.interceptors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.api.garmin.models.OAuth2
import paufregi.connectfeed.data.api.strava.models.Token
import paufregi.connectfeed.data.repository.StravaAuthRepository
import javax.inject.Inject

class StravaAuthInterceptor @Inject constructor(
    private val stravaRepo: StravaAuthRepository,
    private val clientId: String,
    private val clientSecret: String,
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val res = runBlocking(Dispatchers.IO) { getOrAccessToken() }
        return when (res) {
            is Result.Failure -> failedResponse(request, res.reason)
            is Result.Success -> chain.proceed(authRequest(request, res.data.accessToken))
        }
    }


    private suspend fun getOrAccessToken(): Result<Token> {
        val token = stravaRepo.getToken().firstOrNull()

        if (token != null && !token.isExpired()) return Result.Success(token)

        val code = stravaRepo.getCode().firstOrNull() ?: return Result.Failure("No code found")

        return when (token) {
            null -> stravaRepo.exchange(clientId, clientSecret, code)
            else -> stravaRepo.refreshAccessToken(clientId, clientSecret, token.refreshToken)
        }.onSuccess { stravaRepo.saveToken(it) }
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