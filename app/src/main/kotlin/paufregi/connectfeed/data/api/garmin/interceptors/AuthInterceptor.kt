package paufregi.connectfeed.data.api.garmin.interceptors

import android.util.Log
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
    private val repo: AuthRepository,
    private val clientId: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response  =
        runBlocking(Dispatchers.IO) {
            getOrRefreshToken().fold(
                onSuccess = { chain.proceed(authRequest(chain.request(), it.accessToken)) },
                onFailure = { failedAuthResponse(chain.request(), it.message ?: "Unknown error") }
            )
        }

    private suspend fun getOrRefreshToken(): Result<AuthToken> =
        repo.getGarminToken().firstOrNull()?.let { token ->
            if (!token.isExpired()) { Result.success(token) }
            else {
                repo.refreshGarminToken(token.refreshToken, clientId)
                    .onSuccess { repo.saveGarminToken(it) }
                    .onFailure { Log.e("AuthInterceptor", "Failed to refresh token: ${it.message} - ${it.cause}") }
            }
        } ?: Result.failure("No token found")
}