package paufregi.connectfeed.data.api.strava.interceptors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.strava.models.Token
import paufregi.connectfeed.data.repository.StravaAuthRepository
import javax.inject.Inject
import javax.inject.Named

class StravaAuthInterceptor @Inject constructor(
    private val stravaRepo: StravaAuthRepository,
    @Named("StravaClientId") val clientId: String,
    @Named("StravaClientSecret") val clientSecret: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val res = runBlocking(Dispatchers.IO) { getOrRefreshToken() }

        return when (res.isSuccess) {
            true -> chain.proceed(authRequest(request, res.getOrNull()?.accessToken))
            false -> failedResponse(request, res.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    private suspend fun getOrRefreshToken(): Result<Token> {
        val token = stravaRepo.getToken().firstOrNull()

        if (token == null) return Result.failure("No token found")
        if (!token.isExpired()) return Result.success(token)

        return stravaRepo.refresh(clientId, clientSecret, token.refreshToken)
            .onSuccess { stravaRepo.saveToken(it) }
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