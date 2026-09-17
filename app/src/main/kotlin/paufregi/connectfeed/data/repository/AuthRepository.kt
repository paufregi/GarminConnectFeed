package paufregi.connectfeed.data.repository

import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.models.LoginRequest
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.datastore.AuthStore
import javax.inject.Inject
import paufregi.connectfeed.data.api.garmin.models.AuthToken as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthToken as StravaAuthToken

class AuthRepository @Inject constructor(
    private val garminSSO: GarminSSO,
    private val garminAuth: GarminAuth,
    private val stravaAuth: StravaAuth,
    private val authStore: AuthStore,
) {
    // GARMIN
    suspend fun garminLogin(
        username: String,
        password: String
    ): Result<String> =
        garminSSO.login(LoginRequest(username, password))
            .toResult()
            .andThen { response ->
                when (response.responseStatus.type) {
                    "SUCCESSFUL" -> {
                        response.serviceTicketId?.let { Result.success(it) }
                            ?: Result.failure("Login failed - no ticket found")
                    }
                    "INVALID_USERNAME_PASSWORD" -> Result.failure("Invalid username or password")
                    "CAPTCHA_REQUIRED" -> Result.failure("CAPTCHA required - can't continue login")
                    else -> Result.failure("Login failed")
                }
            }

    suspend fun garminExchangeToken(
        ticket: String,
        clientId: String
    ): Result<GarminAuthToken> =
        garminAuth.exchange(GarminAuth.buildBasicAuth(clientId), clientId, ticket)
            .toResult()

    suspend fun garminRefreshToken(
        refreshToken: String,
        clientId: String
    ): Result<GarminAuthToken> =
        garminAuth.refresh(GarminAuth.buildBasicAuth(clientId), clientId, refreshToken)
            .toResult()

    fun getUser() = authStore.user
    fun getGarminToken() = authStore.garminToken

    suspend fun saveUser(user: User) = authStore.saveUser(user)
    suspend fun saveGarminToken(token: GarminAuthToken) = authStore.saveGarminToken(token)


    // STRAVA
    suspend fun stravaExchangeToken(clientId: String, clientSecret: String, code: String) =
        stravaAuth.exchange(clientId, clientSecret, code).toResult()

    suspend fun stravaRefreshToken(clientId: String, clientSecret: String, refreshToken: String) =
        stravaAuth.refresh(clientId, clientSecret, refreshToken).toResult()

    fun getStravaToken() = authStore.stravaToken

    suspend fun saveStravaToken(token: StravaAuthToken) = authStore.saveStravaToken(token)

    suspend fun clearStravaToken() = authStore.clearStravaToken()

    suspend fun isStravaLogged() = authStore.stravaToken.firstOrNull()?.let { true } ?: false


    // CLEAN UP
    suspend fun clear() = authStore.clear()
}